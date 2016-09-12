package com.algolia.instantsearch.strategies;

import com.algolia.instantsearch.Searcher;
import com.algolia.instantsearch.events.ErrorEvent;
import com.algolia.instantsearch.events.ResetEvent;
import com.algolia.search.saas.AlgoliaException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * This {@link SearchStrategy} fires the first time a search is requested,
 * then once every X characters.
 */
public class EveryXCharStrategy implements SearchStrategy {
    public final int x;
    private final EventBus bus;

    private boolean first = true;

    public EveryXCharStrategy(int x) {
        this.x = x;
        (bus = EventBus.getDefault()).register(this);
    }

    //With x=3
    //Types a 5 char long word = res at 1, 4,
    //Pastes a 5 char word = res at 5
    //Types 5, gets results, deletes 3

    @Override
    public boolean search(Searcher searcher, String queryString) {
        final int count = queryString.length();
        if (first || count % x == 1) { // Search the first time, then every X characters
            first = false;
            return true;
        } else {
            bus.post(new ErrorEvent(new AlgoliaException("EveryXCharStrategy: Blocked request of length " + count), searcher.getQuery(), searcher.getLastRequestNumber()));
        }

        if (count == 0) {
            first = true; //reset first time on empty string
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
