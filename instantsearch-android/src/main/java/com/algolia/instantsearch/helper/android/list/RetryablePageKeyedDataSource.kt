package com.algolia.instantsearch.helper.android.list

import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.withContext

/**
 * A [PageKeyedDataSource] with retry capability.
 */
public abstract class RetryablePageKeyedDataSource<Key, Value>(private val retryDispatcher: CoroutineDispatcher) :
    PageKeyedDataSource<Key, Value>() {

    internal var retry: (() -> Any)? = null

    public suspend fun retry() {
        val prevRetry = retry
        retry = null
        if (prevRetry != null) {
            withContext(retryDispatcher) {
                prevRetry()
            }
        }
    }

    /**
     * Retries the latest call.
     */
    public fun retryAsync() {
        val prevRetry = retry
        retry = null
        if (prevRetry != null) {
            retryDispatcher.asExecutor().execute {
                prevRetry()
            }
        }
    }
}
