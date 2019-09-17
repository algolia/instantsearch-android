package com.algolia.instantsearch.core.loading

import com.algolia.instantsearch.core.Callback

/**
 * A View that can display a loading indicator.
 */
public interface LoadingView {

    /**
     * A callback that you must call when the user triggers a reload.
     */
    var onReload: Callback<Unit>?

    /**
     * Updates the loading indicator.
     *
     * @param isLoading if `true`, we are still waiting for at least one search response.
     */
    public fun setIsLoading(isLoading: Boolean)
}