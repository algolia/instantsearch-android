package com.algolia.instantsearch.strategies;

/**
 * This {@link SearchStrategy} only fires requests when enabled.
 */
public class OnDemandStrategy implements SearchStrategy {
    private boolean enabled = false;

    @Override
    public boolean search(com.algolia.instantsearch.Searcher searcher, String queryString) {
        if (enabled) {
            return true;
        } else {
            searcher.postError("OnDemandStrategy: Search currently disabled.");
        }
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
