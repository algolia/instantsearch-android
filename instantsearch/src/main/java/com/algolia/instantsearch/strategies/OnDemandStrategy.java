package com.algolia.instantsearch.strategies;

import com.algolia.instantsearch.Searcher;

/**
 * This {@link SearchStrategy} only fires requests when enabled.
 */
public class OnDemandStrategy implements SearchStrategy {
    private final Searcher searcher;
    private boolean enabled = false;

    public OnDemandStrategy(Searcher searcher) {
        this.searcher = searcher;
    }

    @Override
    public boolean beforeSearch(Searcher searcher, String queryString) {
        if (enabled) {
            return true;
        } else {
            searcher.postErrorEvent("OnDemandStrategy: Search currently disabled.");
        }
        return false;
    }

    /**
     * Triggers a search request with searcher's current state.
     */
    public void triggerSearch() {
        searcher.search();
    }

    /**
     * Enables this strategy, future requests will be fired.
     */
    public void enable() {
        this.enabled = true;
    }

    /**
     * Disables this strategy, future requests will be blocked.
     */
    public void disable() {
        this.enabled = false;
    }

    /**
     * Enables this strategy and triggers a search request.
     */
    public void enableAndSearch() {
        enable();
        triggerSearch();
    }

    public boolean isEnabled() {
        return enabled;
    }
}
