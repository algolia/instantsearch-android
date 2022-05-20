package com.algolia.instantsearch.examples.android.directory

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.android.highlighting.toSpannedString
import com.algolia.instantsearch.android.inflate
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.examples.android.R

class DirectoryAdapter : ListAdapter<DirectoryItem, DirectoryViewHolder>(DirectoryAdapter),
    HitsView<DirectoryItem> {

    private enum class ViewType {
        Header, Item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirectoryViewHolder {
        return when (ViewType.values()[viewType]) {
            ViewType.Header -> DirectoryViewHolder.Header(parent.inflate(R.layout.list_item_header) as TextView)
            ViewType.Item -> DirectoryViewHolder.Item(parent.inflate(R.layout.list_item_small))
        }
    }

    override fun onBindViewHolder(holder: DirectoryViewHolder, position: Int) {
        val item = getItem(position)

        when (holder) {
            is DirectoryViewHolder.Header -> holder.bind(item as DirectoryItem.Header)
            is DirectoryViewHolder.Item -> holder.bind(item as DirectoryItem.Item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)

        return when (item) {
            is DirectoryItem.Header -> ViewType.Header
            is DirectoryItem.Item -> ViewType.Item
        }.ordinal
    }

    override fun setHits(hits: List<DirectoryItem>) {
        submitList(hits)
    }

    companion object : DiffUtil.ItemCallback<DirectoryItem>() {

        override fun areItemsTheSame(oldItem: DirectoryItem, newItem: DirectoryItem): Boolean {
            return oldItem is DirectoryItem.Item
                && newItem is DirectoryItem.Item
                && oldItem.hit.objectID == newItem.hit.objectID
        }

        override fun areContentsTheSame(oldItem: DirectoryItem, newItem: DirectoryItem): Boolean {
            return oldItem == newItem
        }
    }
}

sealed class DirectoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    data class Header(val view: TextView) : DirectoryViewHolder(view) {

        fun bind(item: DirectoryItem.Header) {
            view.text = item.name
        }
    }

    data class Item(val view: View) : DirectoryViewHolder(view) {

        fun bind(item: DirectoryItem.Item) {
            val text = item.hit.highlightedName?.toSpannedString() ?: item.hit.name
            view.findViewById<TextView>(R.id.itemName).text = text

            view.setOnClickListener {
                view.context.navigateTo(item)
            }
        }
    }
}
