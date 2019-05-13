package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.helper.filter.state.Filters


public interface Searcher {

    fun search(filters: Filters)
    fun cancel()
}