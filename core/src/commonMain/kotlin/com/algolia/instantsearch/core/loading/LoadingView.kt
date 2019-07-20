package com.algolia.instantsearch.core.loading

import com.algolia.instantsearch.core.event.Callback


public interface LoadingView {

    var onReload: Callback<Unit>?

    public fun setIsLoading(isLoading: Boolean)
}