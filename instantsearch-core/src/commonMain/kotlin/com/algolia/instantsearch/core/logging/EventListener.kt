package com.algolia.instantsearch.core.logging

public fun interface EventListener {

    /**
     * [onEvent] is always called from the thread the events are emitted from, which is documented
     * for each event. This enables you to potentially block a chain of events, waiting for some
     * pre work to be done.
     */
    public fun onEvent(event: Event)

    public sealed class Event {

        /**
         * Invoked when a response fails to be read.
         */
        public class ResponseFailed(public val throwable: Throwable) : Event()
    }

    public companion object {

        /**
         * No-op implementation of [EventListener].
         */
        public val None: EventListener = EventListener {
            // No-op
        }
    }
}
