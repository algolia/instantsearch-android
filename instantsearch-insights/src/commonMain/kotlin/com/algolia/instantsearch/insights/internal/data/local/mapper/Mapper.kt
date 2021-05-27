package com.algolia.instantsearch.insights.internal.data.local.mapper

internal interface Mapper<T, R> {

    fun map(input: T): R

    fun unmap(input: R): T
}
