package com.algolia.instantsearch.demo.list.paging

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.dip
import com.algolia.instantsearch.helper.android.inflate


class PagingMultipleIndexAdapter : ListAdapter<PagingMultipleIndexItem, PagingMultipleIndexViewHolder>(this) {

    private enum class ViewType {
        Header,
        Movies,
        Actors
    }

    private fun recyclerView(parent: ViewGroup): RecyclerView {
        return RecyclerView(parent.context).also {
            it.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            it.setPadding(parent.context.dip(8), 0, parent.context.dip(8), 0)
            it.itemAnimator = null
            it.clipToPadding = false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagingMultipleIndexViewHolder {
        return when (ViewType.values()[viewType]) {
            ViewType.Header -> PagingMultipleIndexViewHolder.Header(
                parent.inflate<TextView>(R.layout.header_item)
            )
            ViewType.Movies -> PagingMultipleIndexViewHolder.Movies(
                recyclerView(parent)
            )
            ViewType.Actors -> PagingMultipleIndexViewHolder.Actors(
                recyclerView(parent)
            )
        }
    }

    override fun onBindViewHolder(holder: PagingMultipleIndexViewHolder, position: Int) {
        val item = getItem(position)

        when (holder) {
            is PagingMultipleIndexViewHolder.Actors -> holder.bind(item as PagingMultipleIndexItem.Actors)
            is PagingMultipleIndexViewHolder.Movies -> holder.bind(item as PagingMultipleIndexItem.Movies)
            is PagingMultipleIndexViewHolder.Header -> holder.bind(item as PagingMultipleIndexItem.Header)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is PagingMultipleIndexItem.Header -> ViewType.Header
            is PagingMultipleIndexItem.Movies -> ViewType.Movies
            is PagingMultipleIndexItem.Actors -> ViewType.Actors
        }.ordinal
    }

    private companion object : DiffUtil.ItemCallback<PagingMultipleIndexItem>() {

        override fun areItemsTheSame(oldItem: PagingMultipleIndexItem, newItem: PagingMultipleIndexItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PagingMultipleIndexItem, newItem: PagingMultipleIndexItem): Boolean {
            return oldItem == newItem
        }
    }
}