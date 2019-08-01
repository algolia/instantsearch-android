package com.algolia.instantsearch.core.connection


public interface Connection {

    public val isConnected: Boolean

    public fun connect()

    public fun disconnect()
}