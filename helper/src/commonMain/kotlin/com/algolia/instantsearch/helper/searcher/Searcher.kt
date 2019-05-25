package com.algolia.instantsearch.helper.searcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job


public interface Searcher {

    val coroutineScope: CoroutineScope
    val dispatcher: CoroutineDispatcher

    fun setQuery(text: String?)
    fun search(): Job
    fun cancel()
}