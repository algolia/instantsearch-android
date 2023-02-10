package com.algolia.instantsearch.searcher.internal

import com.algolia.instantsearch.extension.telemetrySchema
import com.algolia.instantsearch.util.algoliaAgent
import com.algolia.search.dsl.requestOptions
import com.algolia.search.transport.RequestOptions

internal fun RequestOptions?.withAlgoliaAgent(): RequestOptions {
    return requestOptions(this) {
        parameter("X-Algolia-Agent", buildString {
            append(algoliaAgent("InstantSearchAndroid"))
            telemetrySchema()?.let { append("; $it") }
        })
    }
}
