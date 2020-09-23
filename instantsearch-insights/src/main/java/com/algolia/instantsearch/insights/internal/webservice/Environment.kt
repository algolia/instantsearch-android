package com.algolia.instantsearch.insights.internal.webservice

internal enum class Environment(baseUrl: String) {
    Prod("https://insights.algolia.io"),
    Debug("http://localhost:8080");

    val url: String = "$baseUrl/1/events"
}
