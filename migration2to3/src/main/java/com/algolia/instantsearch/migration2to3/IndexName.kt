package com.algolia.instantsearch.migration2to3



internal object Route {
    const val EventsV1: String = "1/events"
}


public fun String.toIndexName(): IndexName {
    return this
}

public typealias IndexName = String
