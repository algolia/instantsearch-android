package com.algolia.instantsearch.android.swiperefreshlayout

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.algolia.instantsearch.core.loading.LoadingView

public class LoadingViewSwipeRefreshLayout(
    public val swipeRefreshLayout: SwipeRefreshLayout,
) : LoadingView {

    override var onReload: ((Unit) -> Unit)? = null

    init {
        swipeRefreshLayout.setOnRefreshListener { onReload?.invoke(Unit) }
    }

    override fun setIsLoading(isLoading: Boolean) {
        swipeRefreshLayout.isRefreshing = isLoading
    }
}
