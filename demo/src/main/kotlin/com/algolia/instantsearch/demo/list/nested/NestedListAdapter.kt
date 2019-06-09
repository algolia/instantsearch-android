package com.algolia.instantsearch.demo.list.nested

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.dip
import com.algolia.instantsearch.demo.inflate


class NestedListAdapter : ListAdapter<NestedListItem, NestedListViewHolder>(this) {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NestedListViewHolder {
        return when (ViewType.values()[viewType]) {
            ViewType.Header -> NestedListViewHolder.Header(parent.inflate<TextView>(R.layout.header_item))
            ViewType.Movies -> NestedListViewHolder.Movies(recyclerView(parent))
            ViewType.Actors -> NestedListViewHolder.Actors(recyclerView(parent))
        }
    }

    override fun onBindViewHolder(holder: NestedListViewHolder, position: Int) {
        val item = getItem(position)

        when (holder) {
            is NestedListViewHolder.Actors -> holder.bind(item as NestedListItem.Actors)
            is NestedListViewHolder.Movies -> holder.bind(item as NestedListItem.Movies)
            is NestedListViewHolder.Header -> holder.bind(item as NestedListItem.Header)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is NestedListItem.Header -> ViewType.Header
            is NestedListItem.Movies -> ViewType.Movies
            is NestedListItem.Actors -> ViewType.Actors
        }.ordinal
    }

    private companion object : DiffUtil.ItemCallback<NestedListItem>() {

        override fun areItemsTheSame(oldItem: NestedListItem, newItem: NestedListItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: NestedListItem, newItem: NestedListItem): Boolean {
            return oldItem == newItem
        }
    }
}