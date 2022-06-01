package com.algolia.instantsearch.searcher.internal

import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.extension.printDebug
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineExceptionHandler

/**
 * Searcher exception handler.
 * Posts the error and reports search state.
 */
internal class SearcherExceptionHandler<R>(
    private val searcher: Searcher<R>,
) : AbstractCoroutineContextElement(CoroutineExceptionHandler), CoroutineExceptionHandler {

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        printDebug(exception.stackTraceToString())
        searcher.error.value = exception
        searcher.isLoading.value = false
    }
}
