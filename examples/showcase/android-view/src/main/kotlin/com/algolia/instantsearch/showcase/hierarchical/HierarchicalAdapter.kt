package com.algolia.instantsearch.showcase.hierarchical

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.hierarchical.HierarchicalItem
import com.algolia.instantsearch.hierarchical.HierarchicalView
import com.algolia.instantsearch.showcase.databinding.ListItemSelectableBinding
import com.algolia.instantsearch.showcase.layoutInflater

class HierarchicalAdapter : ListAdapter<HierarchicalItem, HierarchicalViewHolder>(diffUtil),
    HierarchicalView {

    override var onSelectionChanged: Callback<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HierarchicalViewHolder {
        return HierarchicalViewHolder(
            ListItemSelectableBinding.inflate(parent.layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HierarchicalViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item) { onSelectionChanged?.invoke(item.facet.value) }
    }

    override fun setTree(tree: List<HierarchicalItem>) {
        submitList(tree)
    }

    companion object {

        private val diffUtil = object : DiffUtil.ItemCallback<HierarchicalItem>() {

            override fun areItemsTheSame(
                oldItem: HierarchicalItem,
                newItem: HierarchicalItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: HierarchicalItem,
                newItem: HierarchicalItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
