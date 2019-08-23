package com.algolia.instantsearch.core.searcher

import kotlinx.coroutines.*


public class Debouncer(
    public val debounceTimeInMillis: Long
) {
    public var job: Job? = null

    public fun debounce(
        coroutineScope: CoroutineScope,
        block: suspend () -> Unit
    ) {
        job?.cancel()
        job = coroutineScope.launch {
            delay(debounceTimeInMillis)
            block()
        }
    }

    public fun <R> debounce(searcher: Searcher<R>, block: suspend Searcher<R>.() -> Unit) {
        debounce(searcher.coroutineScope) { block(searcher) }
    }
}