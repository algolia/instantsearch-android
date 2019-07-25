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

    public operator fun Connection.unaryPlus() {
        connections += this.apply { connect() }
    }

    public operator fun List<Connection>.unaryPlus() {
        connections += this.connect()
    }
}