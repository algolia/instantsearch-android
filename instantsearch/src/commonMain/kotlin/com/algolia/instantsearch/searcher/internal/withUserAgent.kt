package com.algolia.instantsearch.searcher.internal

import com.algolia.instantsearch.core.InstantSearch
import com.algolia.instantsearch.extension.telemetrySchema
import com.algolia.search.dsl.requestOptions
import com.algolia.search.transport.RequestOptions
import io.ktor.http.HttpHeaders

internal fun RequestOptions?.withUserAgent(): RequestOptions {
    return requestOptions(this) {
        header(HttpHeaders.UserAgent, userAgent())
    }
}

private fun userAgent(): String {
    return buildString {
        append("$osVersion; ${InstantSearch.userAgent}")
        telemetrySchema()?.let { append("; $it") }
    }
}
