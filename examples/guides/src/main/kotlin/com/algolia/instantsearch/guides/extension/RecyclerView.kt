package com.algolia.instantsearch.guides.extension

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.android.list.autoScrollToStart
import com.algolia.instantsearch.guides.model.Product

internal fun RecyclerView.configure(recyclerViewAdapter: RecyclerView.Adapter<*>) {
    visibility = View.VISIBLE
    layoutManager = LinearLayoutManager(context)
    adapter = recyclerViewAdapter
    itemAnimator = null
    autoScrollToStart(recyclerViewAdapter)
}

object ProductDiffUtil : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product) = oldItem.objectID == newItem.objectID
    override fun areContentsTheSame(oldItem: Product, newItem: Product) = oldItem == newItem
}
