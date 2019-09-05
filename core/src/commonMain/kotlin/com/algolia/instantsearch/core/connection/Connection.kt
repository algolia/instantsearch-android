package com.algolia.instantsearch.core.connection


/**
 * A connection between two components, that can be disconnected.
 */
public interface Connection {

    public val isConnected: Boolean

    public fun connect()

    public fun disconnect()
}