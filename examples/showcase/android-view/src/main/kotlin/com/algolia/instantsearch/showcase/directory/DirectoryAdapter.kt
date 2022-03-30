package com.algolia.instantsearch.showcase.directory

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.showcase.R
import com.algolia.instantsearch.android.inflate
import com.algolia.instantsearch.showcase.databinding.HeaderItemBinding
import com.algolia.instantsearch.showcase.databinding.ListItemSmallBinding
import com.algolia.instantsearch.showcase.layoutInflater


class DirectoryAdapter : ListAdapter<DirectoryItem, DirectoryViewHolder>(diffUtil), HitsView<DirectoryItem> {

    private enum class ViewType {
        Header,
        Item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirectoryViewHolder {
        return when (ViewType.values()[viewType]) {
            ViewType.Header -> DirectoryViewHolder.Header(
                HeaderItemBinding.inflate(parent.layoutInflater, parent, false)
            )
            ViewType.Item -> DirectoryViewHolder.Item(
                ListItemSmallBinding.inflate(parent.layoutInflater, parent, false)
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

    override fun setHits(hits: List<DirectoryItem>) {
        submitList(hits)
    }

    companion object {

        private val diffUtil = object : DiffUtil.ItemCallback<DirectoryItem>() {

            override fun areItemsTheSame(oldItem: DirectoryItem, newItem: DirectoryItem): Boolean {
                return if (oldItem is DirectoryItem.Item && newItem is DirectoryItem.Item) {
                    return oldItem.hit.objectID == newItem.hit.objectID
                } else false
            }

            @SuppressLint("DiffUtilEquals") // all subclasses are data classes
            override fun areContentsTheSame(oldItem: DirectoryItem, newItem: DirectoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}