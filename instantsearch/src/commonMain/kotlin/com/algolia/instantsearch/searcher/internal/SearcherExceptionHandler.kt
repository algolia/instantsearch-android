package com.algolia.instantsearch.searcher.internal

import com.algolia.instantsearch.core.searcher.Searcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

/**
 * Searcher exception handler.
 * Posts the error and reports search state.
 */
internal class SearcherExceptionHandler<R>(
    private val searcher: Searcher<R>,
) : AbstractCoroutineContextElement(CoroutineExceptionHandler), CoroutineExceptionHandler {

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        searcher.error.value = exception
        searcher.isLoading.value = false
    }
}
