package com.algolia.instantsearch.demo.hierarchical

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.inflate
import com.algolia.instantsearch.helper.hierarchical.HierarchicalItem
import com.algolia.instantsearch.helper.hierarchical.HierarchicalViewImpl


class HierarchicalAdapter : ListAdapter<HierarchicalItem, HierarchicalViewHolder>(diffUtil), HierarchicalViewImpl {

    override var onClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HierarchicalViewHolder {
        return HierarchicalViewHolder(parent.inflate(R.layout.list_item_selectable, false))
    }

    override fun onBindViewHolder(holder: HierarchicalViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item, View.OnClickListener { onClick?.invoke(item.facet.value) })
    }

    override fun setItem(item: List<HierarchicalItem>) {
        submitList(item)
    }

    companion object {

        private val diffUtil = object : DiffUtil.ItemCallback<HierarchicalItem>() {

            override fun areItemsTheSame(oldItem: HierarchicalItem, newItem: HierarchicalItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: HierarchicalItem, newItem: HierarchicalItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}