package com.algolia.instantsearch.core.connection


/**
 * A connection between two components, that can be disconnected.
 */
public interface Connection {

    /** When `true`, the connection is active. */
    public val isConnected: Boolean

    /**
     * Connects the components, allowing one to react to the other's behavior.
     */
    public fun connect()

    /**
     * Disconnects the components, stopping one's reaction(s) to the other's behavior.
     */
    public fun disconnect()
}