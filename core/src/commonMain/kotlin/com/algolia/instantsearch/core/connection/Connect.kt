package com.algolia.instantsearch.core.connection


public fun Connection.asList(): List<Connection> {
    return listOf(this)
}

public fun List<Connection>.connect(): List<Connection> {
    forEach { it.connect() }
    return this
}

public fun List<Connection>.disconnect(): List<Connection> {
    forEach { it.disconnect() }
    return this
}