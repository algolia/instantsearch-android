package com.algolia.instantsearch.android.filter.facet

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.selectable.list.SelectableItem
import com.algolia.instantsearch.filter.facet.FacetListItem
import com.algolia.instantsearch.filter.facet.FacetListView
import com.algolia.instantsearch.filter.Facet

public class FacetListAdapter(
    private val factory: FacetListViewHolder.Factory,
) : ListAdapter<FacetListItem, FacetListViewHolder>(diffUtil),
    FacetListView {

    override var onSelection: Callback<Facet>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacetListViewHolder {
        return factory.createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: FacetListViewHolder, position: Int) {
        val (facet, selected) = getItem(position)

        holder.bind(facet, selected, View.OnClickListener { onSelection?.invoke(facet) })
    }

    override fun setItems(items: List<SelectableItem<Facet>>) {
        submitList(items)
    }

    public companion object {

        private val diffUtil = object : DiffUtil.ItemCallback<FacetListItem>() {

            override fun areItemsTheSame(
                oldItem: FacetListItem,
                newItem: FacetListItem,
            ): Boolean {
                return oldItem::class == newItem::class
            }

            override fun areContentsTheSame(
                oldItem: FacetListItem,
                newItem: FacetListItem,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
