package com.algolia.instantsearch.core.loading

import com.algolia.instantsearch.core.event.Event


public interface LoadingView {

    var onReload: Event<Unit>

    public fun setIsLoading(isLoading: Boolean)
}