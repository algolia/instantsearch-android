package com.algolia.instantsearch.sample.directory

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.algolia.instantsearch.sample.R
import com.algolia.instantsearch.sample.inflate


class DirectoryAdapter : ListAdapter<DirectoryItem, DirectoryViewHolder>(diffUtil) {

    private enum class ViewType {
        Header,
        Item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirectoryViewHolder {
        return when (ViewType.values()[viewType]) {
            ViewType.Header -> DirectoryViewHolder.Header(
                parent.inflate<TextView>(R.layout.header)
            )
            ViewType.Item -> DirectoryViewHolder.Item(
                parent.inflate(R.layout.item)
            )
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

    companion object {

        private val diffUtil = object : DiffUtil.ItemCallback<DirectoryItem>() {

            override fun areItemsTheSame(oldItem: DirectoryItem, newItem: DirectoryItem): Boolean {
                return oldItem::class.isInstance(newItem)
            }

            override fun areContentsTheSame(oldItem: DirectoryItem, newItem: DirectoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}