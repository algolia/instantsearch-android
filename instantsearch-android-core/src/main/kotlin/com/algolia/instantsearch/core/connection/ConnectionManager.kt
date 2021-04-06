package com.algolia.instantsearch.core.connection

/**
 * A [Connection] Manager.
 */
interface ConnectionManager {

    /**
     * Connects all connections.
     */
    public fun connect()

    /**
     * Disconnects all connections.
     */
    public fun disconnect()

    /**
     * Adds a [Connection] to this manager.
     */
    public operator fun plusAssign(connection: Connection)

    /**
     * Adds the given collection of [Connection]s to the manager.
     */
    public operator fun plusAssign(connections: Collection<Connection>)

    /**
     * Disconnects all connections, then removes them all.
     */
    public fun clear()
}
