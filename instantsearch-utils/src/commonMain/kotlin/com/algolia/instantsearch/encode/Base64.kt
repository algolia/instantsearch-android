package com.algolia.instantsearch.encode

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
public fun ByteArray.encodeBase64(): String {
    return Base64.UrlSafe.encode(this)
}

