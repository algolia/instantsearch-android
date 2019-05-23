package com.algolia.instantsearch.core.searcher

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


public class Debouncer(val debounceTimeInMillis: Long) {

    var job: Job? = null

    fun debounce(coroutineScope: CoroutineScope, block: suspend () -> Unit) {
        job?.cancel()
        job = coroutineScope.launch {
            delay(debounceTimeInMillis)
            block()
        }
    }
}