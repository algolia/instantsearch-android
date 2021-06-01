package com.algolia.instantsearch.compose.loading

import com.algolia.instantsearch.compose.loading.internal.LoadingStateImpl
import com.algolia.instantsearch.core.loading.LoadingView

/**
 * [LoadingView] for compose.
 */
public interface LoadingState : LoadingView {

    /**
     * true when loading, false otherwise.
     */
    public val loading: Boolean

    /**
     * Request a reload. Used typically for swipe to refresh.
     */
    public fun reload()
}

/**
 * Creates an instance of [LoadingState].
 *
 * @param initial the initial value
 */
public fun LoadingState(initial: Boolean = false): LoadingState {
    return LoadingStateImpl(initial)
}
