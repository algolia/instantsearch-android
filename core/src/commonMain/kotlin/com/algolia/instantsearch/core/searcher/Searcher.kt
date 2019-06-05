package com.algolia.instantsearch.core.searcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job


public interface Searcher {

    public val coroutineScope: CoroutineScope
    public val dispatcher: CoroutineDispatcher

    public val onLoadingChanged: MutableList<(Boolean) -> Unit>
    public var loading: Boolean

    public fun setQuery(text: String?)
    public fun search(): Job
    public fun cancel()
}