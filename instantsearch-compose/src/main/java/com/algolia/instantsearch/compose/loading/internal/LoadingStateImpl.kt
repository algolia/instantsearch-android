package com.algolia.instantsearch.compose.loading.internal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.algolia.instantsearch.compose.internal.trace
import com.algolia.instantsearch.compose.loading.LoadingState

/**
 * Implementation of [LoadingState]
 *
 * @param loading initial value
 */
internal class LoadingStateImpl(loading: Boolean) : LoadingState {

    override var loading: Boolean by mutableStateOf(loading)
    override var onReload: ((Unit) -> Unit)? = null

    init {
        trace()
    }

    override fun reload() {
        onReload?.invoke(Unit)
    }

    override fun setIsLoading(isLoading: Boolean) {
        this.loading = isLoading
    }
}
