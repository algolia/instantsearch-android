package com.algolia.instantsearch.strategies;

/**
 * This {@link SearchStrategy} only fires requests when enabled.
 */
public class OnDemandStrategy implements SearchStrategy {
    private boolean enabled = false;

    @Override
    public boolean beforeSearch(Searcher searcher, String queryString) {
        if (enabled) {
            return true;
        } else {
            searcher.postErrorEvent("OnDemandStrategy: Search currently disabled.");
        }
        return false;
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
