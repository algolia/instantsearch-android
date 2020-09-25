package com.algolia.instantsearch.insights.internal.database.mapper

internal interface Mapper<T, R> {

    fun map(input: T): R

    fun unmap(input: R): T
}
