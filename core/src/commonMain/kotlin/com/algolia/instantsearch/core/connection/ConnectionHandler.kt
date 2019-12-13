package com.algolia.instantsearch.core.connection


public class ConnectionHandler(
    public val connections: MutableSet<Connection> = mutableSetOf()
) {

    public constructor(
        vararg connections: Connection
    ) : this(connections.toMutableSet())

    init {
        connections.forEach { it.connect() }
    }

    public fun disconnect() {
        connections.forEach { it.disconnect() }
    }

    public operator fun plusAssign(connection: Connection) {
        connections += connection.apply { connect() }
    }

    public operator fun plusAssign(connections: Collection<Connection>) {
        this.connections += connections.apply { forEach { it.connect() } }
    }

    public fun clear() {
        disconnect()
        connections.clear()
    }
}