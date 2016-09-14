package com.algolia.instantsearch.strategies;

import android.os.Handler;

import com.algolia.instantsearch.Searcher;
import com.algolia.instantsearch.events.ResetEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * This {@link SearchStrategy} fires the first time a search is requested,
 * then maximum once every X milliseconds.
 */
public class ThrottleStrategy implements SearchStrategy {
    /** asd*/
    public final int delay;

    private boolean first = true;
    private long lastSearchTimeMillis;

    private final Handler handler;
    private Runnable delayedSearchRunnable;

    public ThrottleStrategy(int delayMillis) {
        this.delay = delayMillis;
        EventBus.getDefault().register(this);
        handler = new Handler();
    }

    @Override
    public boolean beforeSearch(final Searcher searcher, final String queryString) {
        boolean shouldSearch = false;
        final long newSearchTimeMillis = System.nanoTime() / 1000000;
        final long elapsed = newSearchTimeMillis - lastSearchTimeMillis;

        if (queryString.length() == 0) { // on empty search, reset first request status
            first = true;
            shouldSearch = true;
        } else if (first) { // Search the first time
            first = false;
            shouldSearch = true;
        } else {
            if (elapsed > delay) { // Search when the previous one was not sent sooner than delay
                shouldSearch = true;
            } else { // Delay search of the remaining time, and cancel eventual previous delayed search.
                searcher.postErrorEvent("ThrottleStrategy(" + delay + "): Delayed search '" + queryString + "' of " + elapsed + "ms.");
                handler.removeCallbacks(delayedSearchRunnable);
                delayedSearchRunnable = new Runnable() {
                    @Override
                    public void run() {
                        searcher.search(queryString);
                    }
                };
                handler.postDelayed(delayedSearchRunnable, delay - elapsed);
            }
        }

        if (shouldSearch) {
            lastSearchTimeMillis = newSearchTimeMillis; // If we fire the search, save the new time.
            handler.removeCallbacks(delayedSearchRunnable); // And forget the previously delayed search.
        }
        return shouldSearch;
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
