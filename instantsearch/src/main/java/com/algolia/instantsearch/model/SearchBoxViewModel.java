package com.algolia.instantsearch.model;

import android.support.annotation.NonNull;

import com.algolia.instantsearch.events.QueryTextChangeEvent;
import com.algolia.instantsearch.events.QueryTextSubmitEvent;
import com.algolia.instantsearch.helpers.InstantSearch;
import com.algolia.instantsearch.utils.SearchViewFacade;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the logic for linking any kind of {@link SearchViewFacade SearchView} to one or several {@link InstantSearch}.
 */
public class SearchBoxViewModel {

    private final SearchViewFacade searchViewFacade;
    private List<InstantSearch> listeners = new ArrayList<>();

    /**
     * Constructs a {@link SearchBoxViewModel} from an algolia SearchBox.
     *
     * @param searchBox a SearchBox to wrap.
     */
    public SearchBoxViewModel(android.support.v7.widget.SearchView searchBox) {
        this.searchViewFacade = new SearchViewFacade(searchBox);
    }

    /**
     * Constructs a {@link SearchBoxViewModel} from a SearchViewFacade.
     *
     * @param searchViewFacade a SearchViewFacade to wrap.
     */
    public SearchBoxViewModel(@NonNull SearchViewFacade searchViewFacade) {
        this.searchViewFacade = searchViewFacade;
    }

    public void addListener(InstantSearch instantSearch) {
        listeners.add(instantSearch);

        // Called with every added InstantSearch, to replace the SearchView's current listener if any
        setOnQueryTextListener();
    }

    /**
     * Gets the SearchViewFacade underlying this SearchBoxViewModel.
     * @return the {@link SearchBoxViewModel#searchViewFacade}.
     */
    public SearchViewFacade getSearchView() {
        return searchViewFacade;
    }

    private void setOnQueryTextListener() {
        searchViewFacade.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                EventBus.getDefault().post(new QueryTextSubmitEvent());
                // Nothing to do: the search has already been performed by `onQueryTextChange()`.
                // We do try to close the keyboard, though.
                searchViewFacade.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                EventBus.getDefault().post(new QueryTextChangeEvent(newText, searchViewFacade.getSearchView()));

                for (InstantSearch instantSearch : listeners) {
                    if (newText.length() != 0 || instantSearch.hasSearchOnEmptyString()) {
                        instantSearch.search(searchViewFacade.getQuery().toString());
                    }
                }
                return true;
            }
        });
    }
}
