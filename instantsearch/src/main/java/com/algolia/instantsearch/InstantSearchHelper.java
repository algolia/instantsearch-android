package com.algolia.instantsearch;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.algolia.instantsearch.model.Errors;
import com.algolia.instantsearch.utils.LayoutViews;
import com.algolia.instantsearch.views.AlgoliaResultsListener;
import com.algolia.instantsearch.views.Hits;
import com.algolia.instantsearch.views.RefinementList;
import com.algolia.instantsearch.views.SearchBox;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class InstantSearchHelper {
    private SearchView searchView;

    private final Searcher searcher;

    private Menu searchMenu;
    private int searchMenuId;
    private static int itemLayoutId = -42;

    private boolean showProgressBar;
    private int progressBarDelay = 200;

    private boolean shouldResetOnEmptyQuery;

    /**
     * Create and initialize the helper, then link it to the given Activity.
     *
     * @param activity an Activity containing a {@link AlgoliaResultsListener} to update with incoming results.
     * @param searcher the Searcher to use with this activity.
     */
    public InstantSearchHelper(@NonNull final Activity activity, @NonNull final Searcher searcher) {
        this(searcher);

        processActivity(activity);
    }

    /**
     * Create and initialize the helper, then link it to the given Activity.
     *
     * @param resultsListener an AlgoliaResultsListener to update with incoming results.
     * @param searcher        the Searcher to use with this AlgoliaResultsListener.
     */
    public InstantSearchHelper(@NonNull final AlgoliaResultsListener resultsListener, @NonNull final Searcher searcher) {
        this(searcher);
        searcher.registerListener(resultsListener);
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
     * Register the Search Widget of an Activity's Menu to fire queries on text change.
     *
     * @param activity The searchable Activity, see {@link android.app.SearchableInfo}.
     * @param menu     The Menu that contains a search item.
     * @param id       The identifier of the menu's search item.
     */
    public void registerSearchView(@NonNull final Activity activity, @NonNull Menu menu, int id) {
        searchMenu = menu;
        searchMenuId = id;
        registerSearchView(activity, (SearchView) MenuItemCompat.getActionView(menu.findItem(id)));
    }

    /**
     * Registers a {@link SearchView} to fire queries on text change.
     *
     * @param activity   The searchable activity, see {@link android.app.SearchableInfo}.
     * @param searchView a SearchView where the query text will be picked up from.
     */
    public void registerSearchView(@NonNull final Activity activity, @NonNull final SearchView searchView) {
        searchView.setSearchableInfo(((SearchManager) activity.getSystemService(Context.SEARCH_SERVICE)).getSearchableInfo(activity.getComponentName()));
        searchView.setIconifiedByDefault(false);
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
                searcher.search(searchView.getQuery().toString());
                return true;
            }
        });
    }

    /**
     * Initialise a list of facet for the given widget's attribute and operator.
     *
     * @param widget a RefinementList to register as a source of facetRefinements.
     */
    public static void registerRefinementList(@NonNull RefinementList widget, @NonNull Searcher searcher) {
        searcher.addFacet(widget.getAttributeName(), widget.getOperator() == RefinementList.OPERATOR_OR, new ArrayList<String>());
    }

    private void processActivity(@NonNull final Activity activity) {
        View rootView = activity.getWindow().getDecorView().getRootView();
        searchView = getSearchView(rootView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // Ignore submission events: we already update on changes.
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {
                    if (shouldResetOnEmptyQuery) {
                        searcher.reset();
                        searcher.resetListeners();
                    } else {
                        searcher.search(newText);
                    }
                } else {
                    searcher.search(newText);
                }
                return true;
            }
        });

        linkSearchViewToActivity(activity, searchView);

        // Register any AlgoliaResultsListener
        final List<AlgoliaResultsListener> foundListeners = LayoutViews.findByClass(rootView, AlgoliaResultsListener.class);
        if (foundListeners == null || foundListeners.size() == 0) {
            throw new IllegalStateException(Errors.LAYOUT_MISSING_HITS);
        }
        for (AlgoliaResultsListener listener : foundListeners) {
            searcher.registerListener(listener);

            if (listener instanceof Hits) {
                final Hits hits = (Hits) listener;
                searcher.getQuery().setHitsPerPage(hits.getHitsPerPage());

                // Link hits to activity's empty view
                hits.setEmptyView(getEmptyView(rootView));

                itemLayoutId = hits.getLayoutId();

                if (itemLayoutId == -42) {
                    throw new IllegalStateException(Errors.LAYOUT_MISSING_HITS_ITEMLAYOUT);
                }
            } else if (listener instanceof ListView) {
                ((ListView) listener).setEmptyView(getEmptyView(rootView));
            }
        }

        // If we find any RefinementList, add associated facet(s) to the query
        List<RefinementList> refinementLists = LayoutViews.findByClass(rootView, RefinementList.class);
        final boolean hasRefinementList = refinementLists.size() != 0;
        if (hasRefinementList) {
            List<String> refinementAttributes = new ArrayList<>();
            for (RefinementList refinementList : refinementLists) {
                searcher.registerListener(refinementList);
                refinementAttributes.add(refinementList.getAttributeName());
            }

            final String[] facets = refinementAttributes.toArray(new String[refinementAttributes.size()]);
            searcher.getQuery().setFacets(facets);
        }

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if (hasRefinementList) { // we need to remove facetFilters on reset
                    searcher.getQuery().setFacetFilters(new JSONArray());
                }
                searcher.resetListeners();
                return false;
            }
        });

        searcher.initResultsListeners();
    }

    /**
     * Initialise search from the given activity according to its {@link android.app.SearchableInfo searchable info}.
     *
     * @param activity the activity with a {@link SearchView} identified as @+id/searchBox.
     */
    public void initSearchFrom(@NonNull Activity activity) {
        final View rootView = activity.getWindow().getDecorView().getRootView();
        SearchView searchView = getSearchView(rootView);

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
     *
     * @return the registered item layout id, if any.
     * @throws IllegalStateException if no item layout was registered.
     */
    public static int getItemLayoutId() {
        if (itemLayoutId == 0) {
            throw new IllegalStateException(Errors.GET_ITEMLAYOUT_WITHOUT_HITS);
        }
        return itemLayoutId;
    }

    @SuppressLint("InflateParams"/* Giving a root to inflate caused the searchView to break when adding the progressBarView */)
    private void updateProgressBar(SearchView searchView, boolean showProgress) {
        if (!showProgressBar) {
            return;
        }

        if (searchView == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                searchView = (SearchView) searchMenu.findItem(searchMenuId).getActionView();
            }
        }

        if (searchView != null) {
            View searchPlate = searchView.findViewById(R.id.search_plate);
            if (searchPlate == null) {
                Log.e("Searcher", Errors.PROGRESS_WITHOUT_SEARCHVIEW);
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

    private void linkSearchViewToActivity(@NonNull Activity activity, @NonNull SearchView searchView) {
        SearchManager manager = (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(manager.getSearchableInfo(activity.getComponentName()));
    }

    @NonNull
    private static SearchView getSearchView(@NonNull View rootView) {
        SearchView searchView;

        // Either the developer uses our SearchBox
        final List<SearchBox> searchBoxes = LayoutViews.findByClass(rootView, SearchBox.class);
        if (searchBoxes.size() != 0) {
            if (searchBoxes.size() == 1) {
                searchView = searchBoxes.get(0);
            } else { // We should not find more than one SearchBox
                throw new IllegalStateException(Errors.LAYOUT_TOO_MANY_SEARCHBOXES); //TODO: Discuss - any legitimate usecase?
            }
        } else {
            // Or he uses AppCompat's SearchView //TODO: Support standard SearchView?
            final List<SearchView> searchViews = LayoutViews.findByClass(rootView, SearchView.class);
            if (searchViews.size() == 0) { // We should find at least one
                throw new IllegalStateException(Errors.LAYOUT_MISSING_SEARCHBOX);
            } else if (searchViews.size() > 1) { // One of those should have the id @id/searchBox
                searchView = (SearchView) rootView.findViewById(R.id.searchBox);
                if (searchView == null) {
                    throw new IllegalStateException(Errors.LAYOUT_TOO_MANY_SEARCHVIEWS);
                }
            } else {
                searchView = searchViews.get(0);
            }
        }
        return searchView;
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

    /**
     * Tell if this InstantSearchHelper will reset itself when given an empty query string.
     *
     * @return {@code true} when an empty query text triggers a reset of this helper.
     */
    public boolean shouldResetOnEmptyQuery() {
        return shouldResetOnEmptyQuery;
    }

    /**
     * Force reset on empty query instead of searching with an empty query string.
     *
     * @param shouldResetOnEmptyQuery if {@code true}, this helper will reset given an empty query.
     */
    public void setShouldResetOnEmptyQuery(boolean shouldResetOnEmptyQuery) {
        this.shouldResetOnEmptyQuery = shouldResetOnEmptyQuery;
    }
}