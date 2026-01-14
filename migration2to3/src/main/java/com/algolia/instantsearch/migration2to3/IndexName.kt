package com.algolia.instantsearch.migration2to3



public fun String.toIndexName(): IndexName {
    return this
}

public typealias IndexName = String
