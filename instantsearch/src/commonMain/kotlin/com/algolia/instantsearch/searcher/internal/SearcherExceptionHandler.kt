package com.algolia.instantsearch.searcher.internal

import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.extension.Console
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler

/**
 * Searcher exception handler.
 * Posts the error and reports search state.
 */
internal class SearcherExceptionHandler<R>(
    private val searcher: Searcher<R>,
) : AbstractCoroutineContextElement(CoroutineExceptionHandler), CoroutineExceptionHandler {

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        logException(exception)
        searcher.error.value = exception
        searcher.isLoading.value = false
    }

    private fun logException(exception: Throwable) {
        when (exception.cause) {
            is CancellationException -> Console.debug("Search operation interrupted", exception)
            else -> Console.warn("Search operation failed", exception)
        }
    }
}
