package com.algolia.instantsearch.insights.internal.logging

internal expect object InsightsLogger {

    var enabled: MutableMap<String, Boolean>

    fun log(indexName: String, message: String)

    fun log(message: String)
}
