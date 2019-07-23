package com.algolia.instantsearch.core.connection

public typealias Connections = List<Connection>

public fun Connection.autoConnect(connect: Boolean): Connection {
    if (connect) connect()
    return this
}

public fun Connection.asList(): Connections {
    return listOf(this)
}

public fun Connections.connect(): Connections {
    forEach { it.connect() }
    return this
}

public fun Connections.disconnect(): Connections {
    forEach { it.disconnect() }
    return this
}