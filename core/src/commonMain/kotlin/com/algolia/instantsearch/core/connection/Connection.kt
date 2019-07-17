package com.algolia.instantsearch.core.connection


public interface Connection {

    public var isConnected: Boolean

    public fun connect() {
        isConnected = true
    }

    public fun disconnect() {
        isConnected = false
    }
}