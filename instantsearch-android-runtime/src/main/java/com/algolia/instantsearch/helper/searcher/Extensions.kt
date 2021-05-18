package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.core.InstantSearch
import com.algolia.instantsearch.helper.searcher.internal.osVersion
import com.algolia.search.dsl.requestOptions
import com.algolia.search.model.Attribute
import com.algolia.search.model.params.CommonSearchParameters
import com.algolia.search.transport.RequestOptions
import io.ktor.http.HttpHeaders

public fun CommonSearchParameters.addFacet(vararg attribute: Attribute) {
    facets = facets.orEmpty().toMutableSet().also {
        it += attribute
    }
}

public fun CommonSearchParameters.removeFacet(attribute: Attribute) {
    facets = facets.orEmpty().toMutableSet().also {
        it -= attribute
    }
}

internal fun RequestOptions?.withUserAgent(): RequestOptions {
    return requestOptions(this) {
        header(HttpHeaders.UserAgent, "$osVersion; ${InstantSearch.userAgent}")
    }
}
