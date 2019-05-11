package com.algolia.instantsearch.demo.selectable.facet

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.inflate
import com.algolia.search.model.search.Facet
import selectable.facet.SelectableFacet
import selectable.facet.SelectableFacetsView


class SelectableFacetsAdapter :
    ListAdapter<SelectableFacet, SelectableFacetViewHolder>(diffUtil),
    SelectableFacetsView {

    override var onClick: ((Facet) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectableFacetViewHolder {
        return SelectableFacetViewHolder(parent.inflate(R.layout.selectable_item))
    }

    override fun onBindViewHolder(holder: SelectableFacetViewHolder, position: Int) {
        val (facet, selected) = getItem(position)

        holder.bind(facet, selected, View.OnClickListener { onClick?.invoke(facet) })
    }

    override fun setSelectableItems(selectableItems: List<SelectableFacet>) {
        submitList(selectableItems)
    }

    companion object {

        private val diffUtil = object : DiffUtil.ItemCallback<SelectableFacet>() {

            override fun areItemsTheSame(
                oldItem: SelectableFacet,
                newItem: SelectableFacet
            ): Boolean {
                return oldItem::class == newItem::class
            }

            override fun areContentsTheSame(
                oldItem: SelectableFacet,
                newItem: SelectableFacet
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}