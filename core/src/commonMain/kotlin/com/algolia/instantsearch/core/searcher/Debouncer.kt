package com.algolia.instantsearch.core.searcher

import kotlinx.coroutines.*


public class Debouncer(
    public val debounceTimeInMillis: Long
) {

    public var job: Job? = null

    public fun debounce(coroutineScope: CoroutineScope, dispatcher: CoroutineDispatcher, block: suspend () -> Unit) {
        job?.cancel()
        job = coroutineScope.launch(dispatcher) {
            delay(debounceTimeInMillis)
            block()
        }
    }
}