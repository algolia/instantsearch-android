package com.algolia.instantsearch.core.connection

/** Handles connect and disconnect operations of a set of [Connection]s */
public class ConnectionHandler(
    public val connections: MutableSet<Connection> = mutableSetOf()
) {

    public constructor(
        vararg connections: Connection
    ) : this(connections.toMutableSet())

    init {
        connections.forEach { it.connect() }
    }

    /** Disconnects all [Connection]s */
    public fun disconnect() {
        connections.forEach { it.disconnect() }
    }

    /** Adds a [Connection] to the handler */
    public operator fun plusAssign(connection: Connection) {
        connections += connection.apply { connect() }
    }

    /** Adds a collection of [Connection]s to the handler */
    public operator fun plusAssign(connections: Collection<Connection>) {
        this.connections += connections.onEach { it.connect() }
    }

    /** Disconnects and clears all [Connection]s */
    public fun clear() {
        disconnect()
        connections.clear()
    }
}
