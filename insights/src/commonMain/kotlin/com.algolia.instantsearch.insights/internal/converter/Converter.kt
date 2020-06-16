package com.algolia.instantsearch.insights.internal.converter


internal interface Converter<in I, out O> {

    fun convert(input: I): O
    fun convert(inputs: List<I>): List<O> = inputs.map(::convert)
}
