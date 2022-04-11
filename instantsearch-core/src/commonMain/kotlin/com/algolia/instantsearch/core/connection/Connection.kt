package com.algolia.instantsearch.core.connection

import com.algolia.instantsearch.ExperimentalInstantSearch

/**
 * Represents a link (connection) between two components.
 */
public interface Connection {

    /** True if the established connection is enabled, otherwise false */
    public val isConnected: Boolean

    /** Enable this connection */
    public fun connect()

    /** Disable this connection */
    public fun disconnect()
}

/**
 * Delegate the connection/disconnection operations to a [ConnectionHandler].
 */
@ExperimentalInstantSearch
public fun Connection.handleBy(handler: ConnectionHandler) {
    handler += this
}
