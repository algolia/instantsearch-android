package com.algolia.instantsearch.migration2to3

internal fun interface Converter<I, O> {

    operator fun invoke(input: I): O
}
