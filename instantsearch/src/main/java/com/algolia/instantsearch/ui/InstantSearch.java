package com.algolia.instantsearch.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import com.algolia.instantsearch.R;
import com.algolia.instantsearch.events.QueryTextChangeEvent;
import com.algolia.instantsearch.events.QueryTextSubmitEvent;
import com.algolia.instantsearch.events.ResetEvent;
import com.algolia.instantsearch.helpers.SearchProgressController;
import com.algolia.instantsearch.helpers.Searcher;
import com.algolia.instantsearch.model.AlgoliaErrorListener;
import com.algolia.instantsearch.model.AlgoliaResultListener;
import com.algolia.instantsearch.model.AlgoliaSearcherListener;
import com.algolia.instantsearch.model.Errors;
import com.algolia.instantsearch.ui.utils.LayoutViews;
import com.algolia.instantsearch.ui.utils.SearchViewFacade;
import com.algolia.instantsearch.ui.views.Hits;
import com.algolia.instantsearch.ui.views.RefinementList;
import com.algolia.instantsearch.ui.views.SearchBox;
import com.algolia.instantsearch.ui.views.filters.AlgoliaFacetFilter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Uses the {@link Searcher} to react to changes in your application's interface, like when your user types a new query or interacts with Widgets.
 * You can either use it with a single widget which will receive incoming results, or with several that will interact together in the same activity.
 */
public class InstantSearch {
    /** Delay before displaying progressbar when the current android API does not support animations. {@literal (API < 14)} */
    @SuppressWarnings("WeakerAccess") public static final int DELAY_PROGRESSBAR_NO_ANIMATIONS = 200;

    @Nullable
    private SearchViewFacade searchView;

    @NonNull
    private final Set<View> widgets = new HashSet<>();
    @NonNull
    private final Set<AlgoliaResultListener> resultListeners = new HashSet<>();
    @NonNull
    private final Set<AlgoliaErrorListener> errorListeners = new HashSet<>();

    @NonNull
    private final Searcher searcher;

    private Menu searchMenu;
    private int searchMenuId;
    private static int itemLayoutId = -42;

    private boolean showProgressBar;
    private SearchProgressController progressController;

    private boolean searchOnEmptyString;
    private int progressBarDelay = SearchProgressController.DEFAULT_DELAY;

    /**
     * Constructs the helper, then link it to the given Activity.
     *
     * @param activity   an Activity containing at least one {@link AlgoliaResultListener} to update with incoming results.
     * @param searcher the Searcher to use with this activity.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public InstantSearch(@NonNull final Activity activity, @NonNull final Searcher searcher) {
        this(searcher);

        processActivity(activity);
    }

    /**
     * Constructs the helper, then link it to the given Activity and Menu's searchView.
     *
     * @param activity   an Activity containing at least one {@link AlgoliaResultListener} to update with incoming results.
     * @param menu       the Menu which contains your SearchView.
     * @param menuItemId the SearchView item's {@link android.support.annotation.IdRes id} in your Menu.
     * @param searcher   the Searcher to use with this activity.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public InstantSearch(@NonNull final Activity activity, @NonNull Menu menu, @IdRes int menuItemId, @NonNull final Searcher searcher) {
        this(searcher);

        registerSearchView(activity, menu, menuItemId);
        processActivity(activity);
    }

    private InstantSearch(@NonNull final Searcher searcher) {
        this.searcher = searcher;
        enableProgressBar();
    }

    /**
     * Triggers a new search with the {@link #searchView}'s text.
     */
    public void search() {
        searcher.search();
    }

    /**
     * Registers the Search Widget of an Activity's Menu to trigger search requests on text change.
     *
     * @param activity The searchable Activity, see {@link android.app.SearchableInfo}.
     * @param menu     The Menu that contains a search item.
     * @param id       The identifier of the menu's search item.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public void registerSearchView(@NonNull final Activity activity, @NonNull Menu menu, int id) {
        searchMenu = menu;
        searchMenuId = id;
        final SearchViewFacade actionView = new SearchViewFacade(menu, id);
        registerSearchView(activity, actionView);
    }

    /**
     * Registers a {@link SearchView} to trigger search requests on text change.
     *
     * @param activity   The searchable activity, see {@link android.app.SearchableInfo}.
     * @param searchView a SearchView whose query text will be used.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public void registerSearchView(@NonNull final Activity activity, @NonNull final SearchView searchView) {
        registerSearchView(activity, new SearchViewFacade(searchView));
    }

    /**
     * Registers a {@link android.support.v7.widget.SearchView} to trigger search requests on text change.
     *
     * @param activity   The searchable activity, see {@link android.app.SearchableInfo}.
     * @param searchView a SearchView whose query text will be used.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public void registerSearchView(@NonNull final Activity activity, @NonNull final android.support.v7.widget.SearchView searchView) {
        registerSearchView(activity, new SearchViewFacade(searchView));
    }


    /**
     * Resets the search interface and state via {@link Searcher#reset()}, broadcasting a {@link ResetEvent}.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public void reset() {
        searcher.reset();
        EventBus.getDefault().post(new ResetEvent());
    }

    /**
     * Enables the display of a spinning {@link android.widget.ProgressBar ProgressBar} in the SearchView when waiting for results.
     *
     * @param delay a delay to wait between firing a request and displaying the indicator.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public void enableProgressBar(int delay) {
        enableProgressBar();
        progressBarDelay = delay;
    }

    /**
     * Enables the display of a spinning {@link android.widget.ProgressBar ProgressBar} in the SearchView when waiting for results.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public void enableProgressBar() {
        showProgressBar = true;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            progressBarDelay = DELAY_PROGRESSBAR_NO_ANIMATIONS; // Without animations, a delay is needed to avoid blinking.
        }

        progressController = new SearchProgressController(new SearchProgressController.ProgressListener() {
            @Override
            public void onStart() {
                updateProgressBar(searchView, true);
            }

            @Override
            public void onStop() {
                updateProgressBar(searchView, false);
            }
        }, progressBarDelay);
    }

    /**
     * Disables the {@link android.widget.ProgressBar ProgressBar}, removing it if it is already displayed.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public void disableProgressBar() {
        updateProgressBar(searchView, false);
        progressController.disable();
    }

    /**
     * TODO: Ensure developer calls before displaying widget!
     * Registers your {@link AlgoliaFacetFilter facet filters}, adding them to this InstantSearch's widgets.
     *
     * @param rootView a ViewGroup containing one or several {@link AlgoliaFacetFilter AlgoliaFacetFilters}.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public void registerFacetFilters(@NonNull ViewGroup rootView) {
        final List<AlgoliaFacetFilter> filterViews = LayoutViews.findByClass(rootView, AlgoliaFacetFilter.class);
        if (filterViews.isEmpty()) {
            throw new IllegalStateException(String.format(Errors.LAYOUT_MISSING_ALGOLIAFACETFILTER, rootView));
        }
        registerFacetFilters(filterViews);
    }

    /**
     * Registers your facet filters, adding them to this InstantSearch's widgets.
     *
     * @param filters a List of facet filters.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public void registerFacetFilters(List<AlgoliaFacetFilter> filters) {
        for (final AlgoliaFacetFilter filter : filters) {
            searcher.addFacet(filter.getAttributeName());
            processView(filter);
        }
    }

    /**
     * Gets the {@link android.support.annotation.IdRes IdRes} of the item layout registered by a Hits widget.
     * This method should only be used by the library's internals.
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

    /**
     * Enables or disables the sending of a search request when the SearchView becomes empty (default is true).
     *
     * @param searchOnEmptyString if {@code true}, a request will be fired.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public void setSearchOnEmptyString(boolean searchOnEmptyString) {
        this.searchOnEmptyString = searchOnEmptyString;
    }

    /**
     * Tells if an empty string in the {@link #searchView} is a valid search query.
     *
     * @return {@code true} if an empty string triggers a search request.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public boolean hasSearchOnEmptyString() {
        return searchOnEmptyString;
    }

    private void registerSearchView(@NonNull final Activity activity, @NonNull final SearchViewFacade searchView) {
        this.searchView = searchView;
        searchView.setSearchableInfo(((SearchManager) activity.getSystemService(Context.SEARCH_SERVICE)).getSearchableInfo(activity.getComponentName()));
        searchView.setIconifiedByDefault(false);
        linkSearchViewToSearcher(searchView);
    }

    private void processActivity(@NonNull final Activity activity) {
        View rootView = activity.getWindow().getDecorView().getRootView();
        if (searchView == null) {
            searchView = getSearchView(rootView);
        }
        if (searchView != null) {
            linkSearchViewToSearcher(searchView);
            // Link SearchView to Activity
            SearchManager manager = (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);
            searchView.setSearchableInfo(manager.getSearchableInfo(activity.getComponentName()));
        }

        final List<String> refinementAttributes = new ArrayList<>();

        // Register any AlgoliaResultListener
        final List<AlgoliaResultListener> resultListeners = LayoutViews.findByClass(rootView, AlgoliaResultListener.class);
        if (resultListeners.size() == 0) {
            throw new IllegalStateException(Errors.LAYOUT_MISSING_RESULT_LISTENER);
        }
        for (AlgoliaResultListener listener : resultListeners) {
            processResultListener(rootView, listener, refinementAttributes);
        }

        // Register any AlgoliaErrorListener
        final List<AlgoliaErrorListener> errorListeners = LayoutViews.findByClass(rootView, AlgoliaErrorListener.class);
        for (AlgoliaErrorListener listener : errorListeners) {
            processErrorListener(rootView, listener, refinementAttributes);
        }

        // Register any AlgoliaSearcherListener
        final List<AlgoliaSearcherListener> searcherListeners = LayoutViews.findByClass(rootView, AlgoliaSearcherListener.class);
        for (AlgoliaSearcherListener listener : searcherListeners) {
            processSearcherListener(rootView, listener, refinementAttributes);
        }


        final String[] facets = refinementAttributes.toArray(new String[refinementAttributes.size()]);
        if (facets.length > 0) {
            searcher.addFacet(facets);
        }
    }

    private void processResultListener(@Nullable View rootView, @NonNull AlgoliaResultListener listener, @Nullable List<String> refinementAttributes) {
        if (!resultListeners.contains(listener)) {
            resultListeners.add(listener);
        }
        searcher.registerResultListener(listener);
        processView(rootView, listener, refinementAttributes);
    }

    private void processErrorListener(@Nullable View rootView, @NonNull AlgoliaErrorListener listener, @Nullable List<String> refinementAttributes) {
        if (!errorListeners.contains(listener)) {
            errorListeners.add(listener);
        }
        searcher.registerErrorListener(listener);
        processView(rootView, listener, refinementAttributes);
    }

    private void processSearcherListener(@Nullable View rootView, @NonNull AlgoliaSearcherListener listener, @Nullable List<String> refinementAttributes) {
        listener.initWithSearcher(searcher);
        processView(rootView, listener, refinementAttributes);
    }

    private void processView(Object listener) {
        processView(null, listener, null);
    }

    private void processView(@Nullable View rootView, Object listener, @Nullable List<String> refinementAttributes) {
        if (listener instanceof View) {
            processView(rootView, (View) listener, refinementAttributes);
        }
    }

    private void processView(@Nullable View rootView, View widget, @Nullable List<String> refinementAttributes) {
        if (!widgets.contains(widget)) { // process once each widget
            widgets.add(widget);
            if (widget instanceof Hits) {
                searcher.getQuery().setHitsPerPage(((Hits) widget).getHitsPerPage());

                // Link hits to activity's empty view
                ((Hits) widget).setEmptyView(getEmptyView(rootView));

                itemLayoutId = ((Hits) widget).getLayoutId();

                if (itemLayoutId == -42) {
                    throw new IllegalStateException(Errors.LAYOUT_MISSING_HITS_ITEMLAYOUT);
                }
            } else if (widget instanceof RefinementList) {
                searcher.addFacet(((RefinementList) widget).getAttribute(), ((RefinementList) widget).getOperator() == RefinementList.OPERATOR_OR, new ArrayList<String>());
                if (refinementAttributes != null) {
                    refinementAttributes.add(((RefinementList) widget).getAttribute());
                }
            } else if (widget instanceof ListView) {
                ((ListView) widget).setEmptyView(getEmptyView(rootView));
            }
        }
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
            LinearLayout searchPlate = (LinearLayout) searchView.findViewById(searchPlateId);
            if (searchPlate == null) { // Maybe it is an appcompat SearchView?
                searchPlate = (LinearLayout) searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
                if (searchPlate == null) {
                    Log.e("Algolia|InstantSearch", Errors.PROGRESS_WITHOUT_SEARCHPLATE);
                    return;
                }
            }

            View progressBarView = searchPlate.findViewById(R.id.search_progress_bar);
            if (progressBarView != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    progressBarView.animate().setDuration(200).alpha(showProgress ? 1 : 0).start();
                } else { /* No ViewPropertyAnimator before API14 and no animation before API 10, let's just change Visibility */
                    progressBarView.setVisibility(showProgress ? View.VISIBLE : View.GONE);
                }
            } else if (showProgress) {
                searchPlate.setGravity(Gravity.CENTER);
                searchPlate.addView(LayoutInflater.from(searchView.getContext()).inflate(R.layout.loading_icon, null), 1);
            }
        }
    }

    private void linkSearchViewToSearcher(@NonNull final SearchViewFacade searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // SearchView.OnQueryTextListener

            @Override
            public boolean onQueryTextSubmit(String query) {
                EventBus.getDefault().post(new QueryTextSubmitEvent());
                // Nothing to do: the search has already been performed by `onQueryTextChange()`.
                // We do try to close the keyboard, though.
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                EventBus.getDefault().post(new QueryTextChangeEvent(newText));

                if (newText.length() == 0 && searchOnEmptyString) {
                    return true;
                }
                searcher.setQuery(searcher.getQuery().setQuery(searchView.getQuery().toString()))
                        .search();
                return true;
            }
        });
    }

    @Nullable
    private static SearchViewFacade getSearchView(@NonNull View rootView) {
        SearchViewFacade facade = null;

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
                    Log.e("Algolia|InstantSearch", Errors.LAYOUT_MISSING_SEARCHBOX);
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
     * Finds the empty view in the given rootView.
     *
     * @param rootView the topmost view in the view hierarchy of the Activity.
     * @return the empty view if it was in the rootView.
     * @throws RuntimeException if the rootView is null.
     */
    @Nullable
    private static View getEmptyView(@Nullable View rootView) {
        if (rootView == null) {
            throw new RuntimeException("A null rootView was passed to getEmptyView, but Hits/RefinementList require one.");
        }
        return rootView.findViewById(android.R.id.empty);
    }
}