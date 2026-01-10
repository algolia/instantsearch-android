package com.algolia.instantsearch.searcher.internal

import com.algolia.client.transport.RequestOptions
import com.algolia.instantsearch.extension.telemetrySchema
import com.algolia.instantsearch.migration2to3.requestOptions
import com.algolia.instantsearch.util.algoliaAgent


internal fun RequestOptions?.withAlgoliaAgent(): RequestOptions {
    return requestOptions(this) {
        parameter("X-Algolia-Agent", buildString {
            append(algoliaAgent("InstantSearchAndroid"))
            telemetrySchema()?.let { append("; $it") }
        })
    }
}
