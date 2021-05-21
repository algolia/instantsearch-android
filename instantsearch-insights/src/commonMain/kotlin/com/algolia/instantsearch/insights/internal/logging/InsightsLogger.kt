package com.algolia.instantsearch.insights.internal.logging

import com.algolia.search.model.IndexName

internal expect object InsightsLogger {

    var enabled: MutableMap<IndexName, Boolean>

    fun log(indexName: IndexName, message: String)

    fun log(message: String)
}
