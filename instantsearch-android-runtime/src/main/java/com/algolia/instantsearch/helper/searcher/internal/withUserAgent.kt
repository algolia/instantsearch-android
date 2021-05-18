package com.algolia.instantsearch.helper.searcher.internal

import com.algolia.instantsearch.core.InstantSearch
import com.algolia.search.dsl.requestOptions
import com.algolia.search.transport.RequestOptions
import io.ktor.http.HttpHeaders

internal fun RequestOptions?.withUserAgent(): RequestOptions {
    return requestOptions(this) {
        header(HttpHeaders.UserAgent, "$osVersion; ${InstantSearch.userAgent}")
    }
}
