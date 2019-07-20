package com.algolia.instantsearch.core.connection


internal fun Connection.autoConnect(connect: Boolean): Connection {
    if (connect) connect()
    return this
}

public fun Connection.safeConnect(): Connection {
    if (!isConnected) connect()
    return this
}

public fun Connection.safeDisconnect(): Connection {
    if (isConnected) disconnect()
    return this
}