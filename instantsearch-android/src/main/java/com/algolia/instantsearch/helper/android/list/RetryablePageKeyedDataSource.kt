package com.algolia.instantsearch.helper.android.list

import androidx.paging.PageKeyedDataSource

/**
 * A [PageKeyedDataSource] with retry capability.
 */
public abstract class RetryablePageKeyedDataSource<Key, Value> : PageKeyedDataSource<Key, Value>() {

    internal var retry: (() -> Any)? = null

    /**
     * Retries the latest call.
     */
    public fun retry() {
        val prevRetry = retry
        retry = null
        prevRetry?.invoke()
    }
}
