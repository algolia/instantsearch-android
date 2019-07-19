package com.algolia.instantsearch.core.connection


public fun Connection.autoConnect(connect: Boolean): Connection {
    if (connect && !isConnected) connect()
    return this
}