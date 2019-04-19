package com.algolia.instantsearch.sample.refinementFacet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.algolia.instantsearch.sample.R
import com.algolia.search.model.search.Facet
import refinement.RefinementFacet
import selection.SelectionListView


class SelectionListAdapter :
    ListAdapter<RefinementFacet, RefinementFacetViewHolder>(diffUtil),
    SelectionListView<Facet> {

    private lateinit var onClick: (Facet) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RefinementFacetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.refinement_item, parent, false)

        return RefinementFacetViewHolder(view)
    }

    override fun onBindViewHolder(holder: RefinementFacetViewHolder, position: Int) {
        val (facet, selected) = getItem(position)

        holder.bind(facet, selected, View.OnClickListener { onClick(facet) })
    }

    override fun setSelectableItems(selectableItems: List<RefinementFacet>) {
        submitList(selectableItems)
    }

    override fun onClickItem(onClick: (Facet) -> Unit) {
        this.onClick = onClick
    }

    companion object {

        private val diffUtil = object : DiffUtil.ItemCallback<RefinementFacet>() {

            override fun areItemsTheSame(
                oldItem: RefinementFacet,
                newItem: RefinementFacet
            ): Boolean {
                return oldItem::class == newItem::class
            }

            override fun areContentsTheSame(
                oldItem: RefinementFacet,
                newItem: RefinementFacet
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}