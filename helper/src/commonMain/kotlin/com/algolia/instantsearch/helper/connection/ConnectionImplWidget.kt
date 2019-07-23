package com.algolia.instantsearch.helper.connection

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.ConnectionImpl


public abstract class ConnectionImplWidget : ConnectionImpl() {

    protected abstract val connections: List<Connection>

    override fun connect() {
        super.connect()
        connections.forEach { it.connect() }
    }

    override fun disconnect() {
        super.disconnect()
        connections.forEach { it.disconnect() }
    }
}