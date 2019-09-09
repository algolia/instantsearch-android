package com.algolia.instantsearch.core.connection

import kotlin.jvm.JvmSynthetic


/**
 * A handler that gathers connections, offering a convenient way to disconnect them all.
 */
public class ConnectionHandler(
    connections: MutableSet<Connection> = mutableSetOf()
) {
    
    internal val connections = connections
        @JvmSynthetic get

    public constructor(
        vararg connections: Connection
    ) : this(connections.toMutableSet())

    init {
        connections.forEach { it.connect() }
    }

    /**
     * Disconnects all the [connections] in this handler.
     */
    public fun disconnect() {
        connections.forEach { it.disconnect() }
    }

    /**
     * Adds a connection to this handler.
     *
     * @param connection a new [Connection].
     */
    @JvmSynthetic
    public operator fun plusAssign(connection: Connection) {
        connections += connection.apply { connect() }
    }


    /**
     * Adds several connections to this handler.
     *
     * @param connections some new [Connections][Connection].
     */
    @JvmSynthetic
    public operator fun plusAssign(connections: Collection<Connection>) {
        this.connections += connections.apply { forEach { it.connect() } }
    }

    /**
     * Adds a connection to this handler.
     *
     * @param connections one or several new [Collection(s)][Connection].
     */
    public fun add(vararg connections: Connection) {
        this.connections.addAll(connections)
    }
}