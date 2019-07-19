package com.algolia.instantsearch.helper.android.loading

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.algolia.instantsearch.core.loading.LoadingView


public class LoadingViewSwipeRefreshLayout(
    public val swipeRefreshLayout: SwipeRefreshLayout
) : LoadingView {

    override var reload: ((Unit) -> Unit)? = null

    init {
        swipeRefreshLayout.setOnRefreshListener { reload?.invoke(Unit) }
    }

    override fun setIsLoading(isLoading: Boolean) {
        swipeRefreshLayout.isRefreshing = isLoading
    }
}