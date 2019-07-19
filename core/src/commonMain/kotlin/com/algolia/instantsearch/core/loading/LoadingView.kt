package com.algolia.instantsearch.core.loading


public interface LoadingView {

    var reload: ((Unit) -> Unit)?

    public fun setIsLoading(isLoading: Boolean)
}