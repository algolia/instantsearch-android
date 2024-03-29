package com.algolia.instantsearch.core.searcher

import com.algolia.instantsearch.core.internal.AbortException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

public class Debouncer(
    public val debounceTimeInMillis: Long
) {
    public var job: Job? = null

    public fun debounce(
        coroutineScope: CoroutineScope,
        block: suspend () -> Unit
    ) {
        job?.cancel(cause = AbortException("Debouncer"))
        job = coroutineScope.launch {
            delay(debounceTimeInMillis)
            block()
        }
    }

    public fun <R> debounce(searcher: Searcher<R>, block: suspend Searcher<R>.() -> Unit) {
        debounce(searcher.coroutineScope) { block(searcher) }
    }
}
