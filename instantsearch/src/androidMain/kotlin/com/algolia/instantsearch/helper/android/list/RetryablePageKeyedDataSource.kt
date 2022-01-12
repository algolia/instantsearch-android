package com.algolia.instantsearch.helper.android.list

import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.withContext

/**
 * A [PageKeyedDataSource] with retry capability.
 */
@Deprecated("RetryablePageKeyedDataSource is deprecated and has been replaced by Paginator")
public abstract class RetryablePageKeyedDataSource<Key : Any, Value : Any>(private val retryDispatcher: CoroutineDispatcher) :
    PageKeyedDataSource<Key, Value>() {

    internal var retry: (() -> Any)? = null

    public suspend fun retry() {
        retry?.let { prevRetry ->
            retry = null
            withContext(retryDispatcher) {
                prevRetry()
            }
        }
    }

    /**
     * Retries the latest call.
     */
    public fun retryAsync() {
        retry?.let { prevRetry ->
            retry = null
            retryDispatcher.asExecutor().execute {
                prevRetry()
            }
        }
    }
}
