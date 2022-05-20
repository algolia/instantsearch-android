package com.algolia.instantsearch.core.connection

/**
 * Abstract [Connection] implementation.
 */
public abstract class AbstractConnection : Connection {

    final override var isConnected: Boolean = false
        private set

    override fun connect() {
        isConnected = true
    }

    override fun disconnect() {
        isConnected = false
    }
}

@Deprecated("use AbstractConnection instead", replaceWith = ReplaceWith("AbstractConnection"))
public typealias ConnectionImpl = AbstractConnection
