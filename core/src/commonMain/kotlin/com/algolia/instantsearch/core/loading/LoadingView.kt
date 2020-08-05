package com.algolia.instantsearch.core.loading

import com.algolia.instantsearch.core.Callback


public interface LoadingView {

    public var onReload: Callback<Unit>?

    public fun setIsLoading(isLoading: Boolean)
}
