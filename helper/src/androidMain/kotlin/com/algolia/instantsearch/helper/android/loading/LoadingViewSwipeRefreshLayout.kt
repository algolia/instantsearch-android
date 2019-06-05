package com.algolia.instantsearch.helper.android.loading

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.algolia.instantsearch.core.loading.LoadingView


public class LoadingViewSwipeRefreshLayout(
    public val swipeRefreshLayout: SwipeRefreshLayout
) : LoadingView {

    override var onClick: ((Unit) -> Unit)? = null

    init {
        swipeRefreshLayout.setOnRefreshListener { onClick?.invoke(Unit) }
    }

    override fun setItem(item: Boolean) {
        swipeRefreshLayout.isRefreshing = item
    }
}