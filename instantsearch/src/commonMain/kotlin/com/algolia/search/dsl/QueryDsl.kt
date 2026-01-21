package com.algolia.search.dsl

import com.algolia.client.model.search.SearchParamsObject

public class QueryDsl internal constructor(
    private var params: SearchParamsObject = SearchParamsObject(),
) {
    internal fun update(transform: (SearchParamsObject) -> SearchParamsObject) {
        params = transform(params)
    }

    internal fun build(): SearchParamsObject = params
}

public class AnalyticsTagsDsl internal constructor() {
    internal val tags: MutableList<String> = mutableListOf()

    public operator fun String.unaryPlus() {
        tags.add(this)
    }
}

public fun query(block: QueryDsl.() -> Unit): SearchParamsObject {
    val dsl = QueryDsl()
    dsl.block()
    return dsl.build()
}

public fun QueryDsl.analyticsTags(block: AnalyticsTagsDsl.() -> Unit) {
    val tagsDsl = AnalyticsTagsDsl().apply(block)
    update { it.copy(analyticsTags = tagsDsl.tags) }
}
