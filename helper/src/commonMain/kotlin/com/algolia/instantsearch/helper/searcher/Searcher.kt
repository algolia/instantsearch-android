package com.algolia.instantsearch.helper.searcher

import kotlinx.coroutines.Job


public interface Searcher {

    fun setQuery(text: String?)
    fun search(): Job
    fun cancel()
}