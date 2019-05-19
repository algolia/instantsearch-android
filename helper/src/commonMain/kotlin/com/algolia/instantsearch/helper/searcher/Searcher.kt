package com.algolia.instantsearch.helper.searcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job


public interface Searcher {

    val dispatcher: CoroutineDispatcher

    fun setQuery(text: String?)
    fun search(): Job
    fun cancel()
}