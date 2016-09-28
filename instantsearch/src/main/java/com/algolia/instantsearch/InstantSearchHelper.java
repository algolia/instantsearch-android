package com.algolia.instantsearch;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import com.algolia.instantsearch.events.ResetEvent;
import com.algolia.instantsearch.model.Errors;
import com.algolia.instantsearch.utils.LayoutViews;
import com.algolia.instantsearch.utils.SearchViewFacade;
import com.algolia.instantsearch.views.AlgoliaWidget;
import com.algolia.instantsearch.views.Hits;
import com.algolia.instantsearch.views.RefinementList;
import com.algolia.instantsearch.views.SearchBox;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InstantSearchHelper {
    private SearchViewFacade searchView;
    @NonNull
    private final Set<AlgoliaWidget> widgets = new HashSet<>();
    @NonNull
    private final Searcher searcher;

    private Menu searchMenu;
    private int searchMenuId;
    private static int itemLayoutId = -42;

    private boolean showProgressBar;
    private int progressBarDelay;
    private boolean searchOnEmptyString;

    /**
     * Create and initialize the helper, then link it to the given Activity.
     *
     * @param activity an Activity containing at least one {@link AlgoliaWidget} to update with incoming results.
     * @param searcher the Searcher to use with this activity.
     */
    public InstantSearchHelper(@NonNull final Activity activity, @NonNull final Searcher searcher) {
        this(searcher);

        processActivity(activity);
    }

    /**
     * Create and initialize the helper, then link it to the given Activity and Menu's searchView.
     *
     * @param activity an Activity containing at least one {@link AlgoliaWidget} to update with incoming results.S
     * @param searcher the Searcher to use with this activity.
     */
    public InstantSearchHelper(@NonNull final Activity activity, @NonNull Menu menu, int menuItemId, @NonNull final Searcher searcher) {
        this(searcher);

        registerSearchView(activity, menu, menuItemId);
        processActivity(activity);
    }

    /**
     * Create and initialize the helper, then link it to the given widget.
     *
     * @param widget   an AlgoliaWidget to update with incoming results.
     * @param searcher the Searcher to use with this AlgoliaWidget.
     */
    public InstantSearchHelper(@NonNull final AlgoliaWidget widget, @NonNull final Searcher searcher) {
        this(searcher);
        searcher.registerListener(widget);
    }

    private InstantSearchHelper(@NonNull final Searcher searcher) {
        this.searcher = searcher;
        enableProgressBar();
    }

    /**
     * Start a new search with the searchView's text.
     */
    public void search() {
        searcher.search(searchView.getQuery().toString());
    }

    /**
     * Start a search if the given intent is an ACTION_SEARCH intent.
     *
     * @param intent an Intent that may contain a search query.
     */
    public void search(@NonNull Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchView.setQuery(query, false);
            searchView.clearFocus();
            searcher.search(query);
        }
    }

    /**
     * Resets the search interface and state via {@link Searcher#reset()} and {@link AlgoliaWidget#onReset()}.
     */
    public void reset() {
        searcher.reset();
        for (AlgoliaWidget widget : widgets) {
            widget.onReset();
        }
        EventBus.getDefault().post(new ResetEvent());
    }

    /**
     * Register the Search Widget of an Activity's Menu to fire queries on text change.
     *
     * @param activity The searchable Activity, see {@link android.app.SearchableInfo}.
     * @param menu     The Menu that contains a search item.
     * @param id       The identifier of the menu's search item.
     */
    public void registerSearchView(@NonNull final Activity activity, @NonNull Menu menu, int id) {
        searchMenu = menu;
        searchMenuId = id;
        final SearchViewFacade actionView = new SearchViewFacade(menu, id);
        registerSearchView(activity, actionView);
    }

    /**
     * Registers a {@link SearchView} to fire queries on text change.
     *
     * @param activity   The searchable activity, see {@link android.app.SearchableInfo}.
     * @param searchView a SearchView where the query text will be picked up from.
     */
    public void registerSearchView(@NonNull final Activity activity, @NonNull final SearchViewFacade searchView) {
        this.searchView = searchView;
        searchView.setSearchableInfo(((SearchManager) activity.getSystemService(Context.SEARCH_SERVICE)).getSearchableInfo(activity.getComponentName()));
        searchView.setIconifiedByDefault(false);
        linkSearchViewToSearcher(searchView);
    }

    /**
     * Initialize a list of facet for the given widget's attribute and operator.
     *
     * @param refinementList a RefinementList to register as a source of facetRefinements.
     */
    public void registerRefinementList(@NonNull RefinementList refinementList, @NonNull Searcher searcher) {
        if (!widgets.contains(refinementList)) {
            widgets.add(refinementList);
        }
        searcher.addFacet(refinementList.getAttributeName(), refinementList.getOperator() == RefinementList.OPERATOR_OR, new ArrayList<String>());
    }

    private void processActivity(@NonNull final Activity activity) {
        View rootView = activity.getWindow().getDecorView().getRootView();
        if (searchView == null) {
            searchView = getSearchView(rootView);
        }
        linkSearchViewToSearcher(searchView);
        linkSearchViewToActivity(activity, searchView);

        // Register any AlgoliaWidget
        final List<AlgoliaWidget> foundListeners = LayoutViews.findByClass(rootView, AlgoliaWidget.class);
        if (foundListeners.size() == 0) {
            throw new IllegalStateException(Errors.LAYOUT_MISSING_HITS);
        }
        final List<String> refinementAttributes = new ArrayList<>();
        for (AlgoliaWidget widget : foundListeners) {
            widgets.add(widget);
            searcher.registerListener(widget);
            widget.setSearcher(searcher);

            if (widget instanceof Hits) {
                final Hits hits = (Hits) widget;
                searcher.getQuery().setHitsPerPage(hits.getHitsPerPage());

                // Link hits to activity's empty view
                hits.setEmptyView(getEmptyView(rootView));

                itemLayoutId = hits.getLayoutId();

                if (itemLayoutId == -42) {
                    throw new IllegalStateException(Errors.LAYOUT_MISSING_HITS_ITEMLAYOUT);
                }
            } else if (widget instanceof RefinementList) {
                RefinementList refinementList = (RefinementList) widget;
                searcher.registerListener(refinementList);
                refinementAttributes.add(refinementList.getAttributeName());
                registerRefinementList(refinementList, searcher);
            } else if (widget instanceof ListView) {
                ((ListView) widget).setEmptyView(getEmptyView(rootView));
            }
        }
        final String[] facets = refinementAttributes.toArray(new String[refinementAttributes.size()]);
        if (facets.length > 0) {
            searcher.addFacet(facets);
        }
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if (refinementAttributes.size() != 0) { // we need to remove facetFilters on reset
                    searcher.getQuery().setFacetFilters(new JSONArray());
                }
                return false;
            }
        });
    }

    /**
     * Initialize search from the given activity according to its {@link android.app.SearchableInfo searchable info}.
     *
     * @param activity the activity with a {@link SearchView} identified as @+id/searchBox.
     */
    public void initSearchFrom(@NonNull Activity activity) {
        final View rootView = activity.getWindow().getDecorView().getRootView();
        SearchViewFacade searchView = getSearchView(rootView);

        linkSearchViewToActivity(activity, searchView);
    }

    /**
     * Enable the display of a spinning progressBar in the SearchView when waiting for results.
     *
     * @param delay a delay to wait between firing a request and displaying the indicator.
     */
    public void enableProgressBar(int delay) {
        enableProgressBar();
        progressBarDelay = delay;
    }

    /**
     * Enable the display of a {@link android.widget.ProgressBar} in the SearchView when waiting for results.
     */
    public void enableProgressBar() {
        showProgressBar = true;
        searcher.setProgressStartRunnable(new Runnable() {
            @Override
            public void run() {
                updateProgressBar(searchView, true);
            }
        }, progressBarDelay);
        searcher.setProgressStopRunnable(new Runnable() {
            @Override
            public void run() {
                updateProgressBar(searchView, false);
            }
        });
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            progressBarDelay = 200; // Without animations, a delay is needed to avoid blinking.
        }
    }

    /**
     * Disable the {@link android.widget.ProgressBar}, removing it if it is already displayed.
     */
    public void disableProgressBar() {
        updateProgressBar(searchView, false);
        searcher.setProgressStartRunnable(null);
        searcher.setProgressStopRunnable(null);
    }

    /**
     * Get the {@link android.support.annotation.IdRes} of the item layout registered by a Hits widget.
     * This method should only be used by the library's internals. TODO: Can I enforce that while keeping separated packages?
     *
     * @return the registered item layout id, if any.
     * @throws IllegalStateException if no item layout was registered.
     */
    public static int getItemLayoutId() {
        if (itemLayoutId == 0) {
            throw new IllegalStateException(Errors.HITS_NO_ITEMLAYOUT);
        }
        return itemLayoutId;
    }

    @SuppressLint("InflateParams"/* Giving a root to inflate causes the searchView to break when adding the progressBarView */)
    private void updateProgressBar(@Nullable SearchViewFacade searchView, boolean showProgress) {
        if (!showProgressBar) {
            return;
        }

        if (searchView == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                searchView = new SearchViewFacade(searchMenu, searchMenuId);
            }
        }

        if (searchView != null) {
            int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
            View searchPlate = searchView.findViewById(searchPlateId);
            if (searchPlate == null) {
                Log.e("Searcher", Errors.PROGRESS_WITHOUT_SEARCHPLATE);
                return;
            }

            final View progressBarView = searchPlate.findViewById(R.id.search_progress_bar);
            if (progressBarView != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    progressBarView.animate().setDuration(200).alpha(showProgress ? 1 : 0).start();
                } else { /* No ViewPropertyAnimator before API14 and no animation before API 10, let's just change Visibility */
                    progressBarView.setVisibility(showProgress ? View.VISIBLE : View.GONE);
                }
            } else if (showProgress) {
                ((ViewGroup) searchPlate).addView(LayoutInflater.from(searchView.getContext()).inflate(R.layout.loading_icon, null), 1);
            }
        }
    }

    private void linkSearchViewToSearcher(@NonNull final SearchViewFacade searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // SearchView.OnQueryTextListener

            @Override
            public boolean onQueryTextSubmit(String query) {
                // Nothing to do: the search has already been performed by `onQueryTextChange()`.
                // We do try to close the keyboard, though.
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0 && searchOnEmptyString) {
                    return true;
                }
                searcher.search(searchView.getQuery().toString());
                return true;
            }
        });
    }

    private void linkSearchViewToActivity(@NonNull Activity activity, @NonNull SearchViewFacade searchView) {
        SearchManager manager = (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(manager.getSearchableInfo(activity.getComponentName()));
    }

    /**
     * Enable or disable the sending of a search request when the SearchView becomes empty (defaults to true).
     *
     * @param searchOnEmptyString if true, a request will be fired.
     */
    public void setSearchOnEmptyString(boolean searchOnEmptyString) {
        this.searchOnEmptyString = searchOnEmptyString;
    }

    public boolean searchOnEmptyString() {
        return searchOnEmptyString;
    }

    @NonNull
    private static SearchViewFacade getSearchView(@NonNull View rootView) {
        SearchViewFacade facade;

        // Either the developer uses our SearchBox
        final List<SearchBox> searchBoxes = LayoutViews.findByClass(rootView, SearchBox.class);
        if (searchBoxes.size() != 0) {
            if (searchBoxes.size() == 1) {
                facade = new SearchViewFacade(searchBoxes.get(0));
            } else { // We should not find more than one SearchBox
                throw new IllegalStateException(Errors.LAYOUT_TOO_MANY_SEARCHBOXES);
            }
        } else { // Or he uses a standard SearchView
            final List<SearchView> searchViews = LayoutViews.findByClass(rootView, SearchView.class);
            if (searchViews.size() == 0) { // Or he uses a support SearchView
                final List<android.support.v7.widget.SearchView> supportViews = LayoutViews.findByClass(rootView, android.support.v7.widget.SearchView.class);
                if (supportViews.size() == 0) { // We should find at least one
                    throw new IllegalStateException(Errors.LAYOUT_MISSING_SEARCHBOX);
                } else if (supportViews.size() > 1) { // One of those should have the id @id/searchBox
                    final SearchView labeledSearchView = (SearchView) rootView.findViewById(R.id.searchBox);
                    if (labeledSearchView == null) {
                        throw new IllegalStateException(Errors.LAYOUT_TOO_MANY_SEARCHVIEWS);
                    } else {
                        facade = new SearchViewFacade((SearchView) rootView.findViewById(R.id.searchBox));
                    }
                } else {
                    facade = new SearchViewFacade(supportViews.get(0));
                }
            } else if (searchViews.size() > 1) { // One of those should have the id @id/searchBox
                final SearchView labeledSearchView = (SearchView) rootView.findViewById(R.id.searchBox);
                if (labeledSearchView == null) {
                    throw new IllegalStateException(Errors.LAYOUT_TOO_MANY_SEARCHVIEWS);
                } else {
                    facade = new SearchViewFacade(labeledSearchView);
                }
            } else {
                facade = new SearchViewFacade(searchViews.get(0));
            }
        }
        return facade;
    }

    /**
     * Find the empty view in the given rootView.
     *
     * @param rootView the topmost view in the view hierarchy of the Activity.
     * @return the empty view if it was in the given rootView.
     * @throws IllegalStateException if no empty view can be found.
     */
    @NonNull
    private static View getEmptyView(@NonNull View rootView) {
        View emptyView = rootView.findViewById(R.id.empty);
        if (emptyView == null) {
            throw new IllegalStateException(Errors.LAYOUT_MISSING_EMPTY);
        }
        return emptyView;
    }
}