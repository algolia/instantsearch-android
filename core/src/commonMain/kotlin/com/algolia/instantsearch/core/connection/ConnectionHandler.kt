package com.algolia.instantsearch.core.connection


public class ConnectionHandler(
    private val connections: MutableList<Connection> = mutableListOf()
) {

    public constructor(
        vararg connections: Connection
    ) : this(connections.toMutableList())

    public fun disconnect() {
        connections.disconnect()
    }

    public operator fun Connection.unaryPlus() {
        connections += this.apply { connect() }
    }

    public operator fun List<Connection>.unaryPlus() {
        connections += this.connect()
    }

    companion object : (ConnectionHandler.() -> Unit) -> List<Connection> {

        override fun invoke(connections: ConnectionHandler.() -> Unit): List<Connection> {
            return ConnectionHandler().apply(connections).connections
        }
    }
}