package com.algolia.instantsearch.core

import com.algolia.instantsearch.telemetry.Telemetry

/**
 * Instant Search Telemetry.
 */
public sealed interface InstantSearchTelemetry {

    /**
     * Flag to enable/disable telemetry operations.
     */
    public var enabled: Boolean

    public companion object {

        /**
         * The default instance of [InstantSearchTelemetry].
         */
        public val shared: InstantSearchTelemetry = DelegateTelemetry(Telemetry.shared)
    }
}

/**
 * Delegate class for [Telemetry]
 */
internal class DelegateTelemetry(private val telemetry: Telemetry) : InstantSearchTelemetry {
    override var enabled: Boolean by telemetry::enabled
}
