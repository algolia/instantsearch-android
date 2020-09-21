package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.core.InstantSearch
import com.algolia.search.dsl.requestOptions
import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Query
import com.algolia.search.transport.RequestOptions
import io.ktor.http.HttpHeaders

public fun Query.addFacet(vararg attribute: Attribute) {
    facets = facets.orEmpty().toMutableSet().also {
        it += attribute
    }
}

public fun Query.removeFacet(attribute: Attribute) {
    facets = facets.orEmpty().toMutableSet().also {
        it -= attribute
    }
}

internal fun RequestOptions?.withUserAgent(): RequestOptions {
    return requestOptions(this) {
        header(HttpHeaders.UserAgent, "$osVersion; ${InstantSearch.userAgent}")
    }
}
