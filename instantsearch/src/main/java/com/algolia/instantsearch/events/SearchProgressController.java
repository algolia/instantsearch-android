package com.algolia.instantsearch.events;

import android.os.Handler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * This class lets you specify a listener which will be informed of progress events.
 */
public class SearchProgressController {
    public static final int DEFAULT_DELAY = 0;

    private final ProgressListener listener;
    private final int delay;
    private int currentCount;

    /**
     * Create a controller that will report requests immediately.
     *
     * @param listener a {@link ProgressListener} to notify of progress events.
     */
    public SearchProgressController(ProgressListener listener) {
        this(listener, DEFAULT_DELAY);
    }

    /**
     * Create a controller that will report requests if they don't complete before <code>delay</code>.
     *
     * @param listener a {@link ProgressListener} to notify of progress events.
     * @param delay    a duration in milliseconds to wait before calling {@link ProgressListener#onStart()}.
     */
    public SearchProgressController(ProgressListener listener, int delay) {
        this.listener = listener;
        this.delay = delay;
        enable();
    }

    /**
     * Enable this controller, informing its <code>listener</code> of future events.
     */
    public void enable() {
        EventBus.getDefault().register(this);
    }

    /**
     * Disable this controller, stopping propagation of events to its <code>listener</code>.
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
