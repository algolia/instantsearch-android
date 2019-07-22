package com.algolia.instantsearch.core.connection


public fun Connection.autoConnect(connect: Boolean): Connection {
    if (connect) connect()
    return this
}

public fun <T : Connection> T.safeConnect(): T {
    if (!isConnected) connect()
    return this
}

public fun <T : Connection> T.safeDisconnect(): T {
    if (isConnected) disconnect()
    return this
}

public fun List<Connection>.connect(): List<Connection> {
    forEach { it.connect() }
    return this
}

public fun List<Connection>.disconnect(): List<Connection> {
    forEach { it.disconnect() }
    return this
}

public fun List<Connection>.safeConnect(): List<Connection> {
    forEach { it.safeConnect() }
    return this
}

public fun List<Connection>.safeDisconnect(): List<Connection> {
    forEach { it.safeDisconnect() }
    return this
}