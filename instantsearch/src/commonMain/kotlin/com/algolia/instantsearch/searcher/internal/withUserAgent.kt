package com.algolia.instantsearch.searcher.internal

import com.algolia.client.transport.RequestOptions
import com.algolia.instantsearch.extension.telemetrySchema
import com.algolia.instantsearch.util.algoliaAgent


internal fun RequestOptions?.withAlgoliaAgent(): RequestOptions = RequestOptions(
    headers = mapOf(
        "X-Algolia-Agent" to buildString {
            append(algoliaAgent("InstantSearchAndroid"))
            telemetrySchema()?.let { append("; $it") }
        }
    ),
    urlParameters = this?.urlParameters ?: emptyMap(),
    body = this?.body
)
