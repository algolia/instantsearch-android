package com.algolia.instantsearch.helper.android.list

import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.withContext

/**
 * A [PageKeyedDataSource] with retry capability.
 */
public abstract class RetryablePageKeyedDataSource<Key, Value> : PageKeyedDataSource<Key, Value>() {

    internal var retry: (() -> Any)? = null

    public suspend fun retry() {
        val prevRetry = retry
        retry = null
        if (prevRetry != null) {
            withContext(Dispatchers.IO) {
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
            Dispatchers.IO.asExecutor().execute {
                prevRetry()
            }
        }
    }
}
