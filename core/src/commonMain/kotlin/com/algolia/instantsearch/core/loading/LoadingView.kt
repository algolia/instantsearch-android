package com.algolia.instantsearch.core.loading

import com.algolia.instantsearch.core.CallbackUnit


public interface LoadingView {

    var onReload: CallbackUnit?

    public fun setIsLoading(isLoading: Boolean)
}