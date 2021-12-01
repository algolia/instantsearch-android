package com.algolia.instantsearch.helper.searcher.internal

import com.algolia.instantsearch.core.InstantSearch
import com.algolia.instantsearch.helper.extension.telemetrySchema
import com.algolia.search.dsl.requestOptions
import com.algolia.search.transport.RequestOptions
import io.ktor.http.HttpHeaders

internal fun RequestOptions?.withUserAgentTelemetry(): RequestOptions {
    return requestOptions(this) {
        header(HttpHeaders.UserAgent, userAgent(telemetrySchema()))
    }
}

private fun userAgent(telemetry: String? = null): String {
    return if (telemetry == null) "$osVersion; ${InstantSearch.userAgent}" else "$osVersion; ${InstantSearch.userAgent}; $telemetry"
}
