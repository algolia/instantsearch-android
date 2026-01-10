package com.algolia.instantsearch.migration2to3

import kotlinx.serialization.json.JsonObject

public fun requestOptions(
    requestOptions: RequestOptions? = null,
    block: RequestOptions.() -> Unit,
): RequestOptions {
    return (requestOptions ?: RequestOptions()).apply(block)
}

public typealias RequestOptions = com.algolia.client.transport.RequestOptions

//public class RequestOptions {
//
//    public val headers: MutableMap<String, Any> = mutableMapOf()
//    public val urlParameters: MutableMap<String, Any> = mutableMapOf()
//
//    public var writeTimeout: Long? = null
//    public var readTimeout: Long? = null
//    public var body: JsonObject? = null
//
//    /**
//     * Add a "X-Forwarded-For" header with an [ipAddress] to [headers].
//     */
//    public fun headerForwardedFor(ipAddress: String) {
//        headers[Key.ForwardedFor] = ipAddress
//    }
//
//    /**
//     * Add a "X-Algolia-User-ID" header with an [userId] to [headers].
//     */
//    public fun headerAlgoliaUserId(userId: UserID) {
//        headers[Key.AlgoliaUserID] = userId.raw
//    }
//
//    /**
//     * Add a url parameter with [key] and [value] to [urlParameters].
//     */
//    public fun parameter(key: String, value: Any?) {
//        value?.let { urlParameters[key] = it }
//    }
//
//    /**
//     * Add a header with [key] and [value] to [headers].
//     */
//    public fun header(key: String, value: Any?) {
//        value?.let { headers[key] = it }
//    }
//}
