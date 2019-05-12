package com.algolia.instantsearch.demo.home

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.inflate


class HomeAdapter : ListAdapter<HomeItem, HomeViewHolder>(diffUtil) {

    private enum class ViewType {
        Header,
        Item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return when (ViewType.values()[viewType]) {
            ViewType.Header -> HomeViewHolder.Header(
                parent.inflate<TextView>(R.layout.header)
            )
            ViewType.Item -> HomeViewHolder.Item(
                parent.inflate(R.layout.list_item)
            )
        }
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = getItem(position)

        when (holder) {
            is HomeViewHolder.Header -> holder.bind(item as HomeItem.Header)
            is HomeViewHolder.Item -> holder.bind(item as HomeItem.Item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)

        return when (item) {
            is HomeItem.Header -> ViewType.Header
            is HomeItem.Item -> ViewType.Item
        }.ordinal
    }

    companion object {

        private val diffUtil = object : DiffUtil.ItemCallback<HomeItem>() {

            override fun areItemsTheSame(oldItem: HomeItem, newItem: HomeItem): Boolean {
                return oldItem::class.isInstance(newItem)
            }

            override fun areContentsTheSame(oldItem: HomeItem, newItem: HomeItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}