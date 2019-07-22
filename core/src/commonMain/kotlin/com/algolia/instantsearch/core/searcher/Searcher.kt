package com.algolia.instantsearch.core.searcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job


public interface Searcher<R> {

    public val coroutineScope: CoroutineScope
    public val dispatcher: CoroutineDispatcher

    public var loading: Boolean
    public var response: R?
    public var error: Throwable?

    public val onResponseChanged: MutableList<(R) -> Unit>
    public val onLoadingChanged: MutableList<(Boolean) -> Unit>
    public val onErrorChanged: MutableList<(Throwable) -> Unit>

    public fun setQuery(text: String?)
    public fun searchAsync(): Job
    public suspend fun search(): R
    public fun cancel()
}