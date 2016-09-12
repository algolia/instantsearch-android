package com.algolia.instantsearch.strategies;

import com.algolia.instantsearch.Searcher;
import com.algolia.instantsearch.events.ResetEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * This {@link SearchStrategy} fires the first time a search is requested,
 * then once every X characters.
 */
public class EveryXCharStrategy implements SearchStrategy {
    public final int x;

    private boolean first = true;

    public EveryXCharStrategy(int x) {
        this.x = x;
        EventBus.getDefault().register(this);
    }

    @Override
    public boolean search(Searcher searcher, String queryString) {
        final int count = queryString.length();
        if (count == 0) {
            first = true; //reset first time on empty string
        } else if (first || count % x == 1) { // Search the first time, then every X characters
            first = false;
            return true;
        } else {
            searcher.postErrorEvent("EveryXCharStrategy: Blocked request of length " + count);
        }

        return false;
    }

    /**
     * Subscriber to reset first search when a {@link ResetEvent} is broadcasted.
     *
     * @param event the received event.
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResetEvent(ResetEvent event) {
        first = true;
    }
}
