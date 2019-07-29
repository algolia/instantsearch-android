package com.algolia.instantsearch.core.connection


public class ConnectionHandler(
    private val connections: MutableList<Connection> = mutableListOf()
) {

    public constructor(
        vararg connections: Connection
    ) : this(connections.toMutableList())

    init {
        connections.connect()
    }

    public fun disconnect() {
        connections.disconnect()
    }

    public operator fun plusAssign(connection: Connection) {
        connections += connection.apply { connect() }
    }

    public operator fun plusAssign(connections: Collection<Connection>) {
        this.connections += connections.apply { forEach { it.connect() } }
    }
}