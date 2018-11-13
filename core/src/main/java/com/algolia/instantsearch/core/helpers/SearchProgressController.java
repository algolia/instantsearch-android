package com.algolia.instantsearch.core.helpers;

import android.os.Handler;

import com.algolia.instantsearch.core.events.CancelEvent;
import com.algolia.instantsearch.core.events.ErrorEvent;
import com.algolia.instantsearch.core.events.ResultEvent;
import com.algolia.instantsearch.core.events.SearchEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Watches events and informs its listener when there are one or several ongoing searches.
 */
@SuppressWarnings("UnusedParameters") // binding to EventBus events without using the event objects
public class SearchProgressController {
    /**
     * Default waiting delay: unless specified otherwise, {@link ProgressListener#onStart() listener.onStart()} will be called immediately on any {@link SearchEvent}.
     */
    public static final int DEFAULT_DELAY = 0;

    private final ProgressListener listener;
    private final int delay;
    private int currentCount;

    /**
     * Constructs a controller that will report requests immediately.
     *
     * @param listener a {@link ProgressListener ProgressListener} to notify of progress events.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public SearchProgressController(ProgressListener listener) {
        this(listener, DEFAULT_DELAY);
    }

    /**
     * Constructs a controller that will report requests if they don't complete before <code>delay</code>.
     *
     * @param listener a {@link ProgressListener ProgressListener} to notify of progress events.
     * @param delay    a duration in milliseconds to wait before calling {@link ProgressListener#onStart() listener.onStart()}.
     */
    public SearchProgressController(ProgressListener listener, int delay) {
        this.listener = listener;
        this.delay = delay;
        enable();
    }

    /**
     * Enables this controller, informing its <code>listener</code> of future events.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public void enable() {
        EventBus.getDefault().register(this);
    }

    /**
     * Disables this controller, stopping propagation of events to its <code>listener</code>.
     */
    public void disable() {
        EventBus.getDefault().unregister(this);
        currentCount = 0;
    }

    @Subscribe
    public void onSearch(SearchEvent event) {
        if (delay == 0) {
            listener.onStart();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    listener.onStart();
                }
            }, delay);
        }
        currentCount++;
    }

    @Subscribe
    public void onError(ErrorEvent event) {
        decrementAndCheckCount();
    }

    @Subscribe
    public void onCancel(CancelEvent event) {
        decrementAndCheckCount();
    }

    @Subscribe
    public void onResult(ResultEvent event) {
        decrementAndCheckCount();
    }

    private void decrementAndCheckCount() {
        currentCount--;
        if (currentCount == 0) {
            listener.onStop();
        }
    }

    /**
     * A listener that will be informed of ongoing progress.
     */
    public interface ProgressListener {
        /**
         * Callback method to be invoked when there are ongoing requests (after an eventual <code>delay</code>).
         */
        void onStart();

        /**
         * Callback method to be invoked when all ongoing requests resolve.
         */
        void onStop();
    }
}
