package com.algolia.instantsearch.android.list

import androidx.recyclerview.widget.RecyclerView

public fun RecyclerView.autoScrollToStart(adapter: RecyclerView.Adapter<*>) {
    adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            if (positionStart == 0) {
                scrollToPosition(0)
            }
        }
    })
}
