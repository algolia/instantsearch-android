package com.algolia.instantsearch.core.connection

public open class ConnectionImpl : Connection {

    final override var isConnected: Boolean = false
        private set

    override fun connect() {
        isConnected = true
    }

    override fun disconnect() {
        isConnected = false
    }
}
