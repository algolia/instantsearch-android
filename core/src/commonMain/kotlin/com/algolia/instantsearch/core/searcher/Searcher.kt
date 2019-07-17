package com.algolia.instantsearch.core.searcher

import com.algolia.instantsearch.core.observable.ObservableItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job


public interface Searcher<R> {

    public val coroutineScope: CoroutineScope
    public val dispatcher: CoroutineDispatcher

    public val isLoading: ObservableItem<Boolean>
    public val error: ObservableItem<Throwable?>
    public val response: ObservableItem<R?>

    public fun setQuery(text: String?)
    public fun searchAsync(): Job
    public suspend fun search(): R
    public fun cancel()
}