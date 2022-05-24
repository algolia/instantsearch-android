package com.algolia.instantsearch.searcher.internal

import com.algolia.instantsearch.core.logging.EventListener
import com.algolia.instantsearch.core.logging.EventListener.Event.ResponseFailed
import com.algolia.instantsearch.core.searcher.Searcher
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineExceptionHandler

/**
 * Searcher exception handler.
 * Posts the error and reports search state.
 */
internal class SearcherExceptionHandler<R>(
    private val searcher: Searcher<R>,
    private val eventListener: EventListener,
) : AbstractCoroutineContextElement(CoroutineExceptionHandler), CoroutineExceptionHandler {

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        searcher.error.value = exception
        searcher.isLoading.value = false
        eventListener.onEvent(ResponseFailed(exception))
    }
}
