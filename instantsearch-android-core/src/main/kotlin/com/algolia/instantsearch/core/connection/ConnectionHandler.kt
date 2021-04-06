package com.algolia.instantsearch.core.connection

public class ConnectionHandler(
    public val connections: MutableSet<Connection> = mutableSetOf()
) : ConnectionManager {

    public constructor(
        vararg connections: Connection
    ) : this(connections.toMutableSet())

    init {
        connect()
    }

    override fun connect() {
        connections.forEach { it.connect() }
    }

    override fun disconnect() {
        connections.forEach { it.disconnect() }
    }

    override operator fun plusAssign(connection: Connection) {
        connections += connection.apply { connect() }
    }

    override operator fun plusAssign(connections: Collection<Connection>) {
        this.connections += connections.apply { forEach { it.connect() } }
    }

    override fun clear() {
        disconnect()
        connections.clear()
    }
}
