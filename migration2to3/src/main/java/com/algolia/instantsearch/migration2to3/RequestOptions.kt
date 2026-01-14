package com.algolia.instantsearch.migration2to3

import kotlinx.serialization.json.JsonObject

public fun requestOptions(
    requestOptions: RequestOptions? = null,
    block: RequestOptions.() -> Unit,
): RequestOptions {
    return (requestOptions ?: RequestOptions()).apply(block)
}

public typealias RequestOptions = com.algolia.client.transport.RequestOptions

