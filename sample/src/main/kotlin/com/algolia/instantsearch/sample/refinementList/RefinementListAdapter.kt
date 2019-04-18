package com.algolia.instantsearch.sample.refinementList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.algolia.instantsearch.sample.R
import com.algolia.search.model.search.Facet
import refinement.RefinementListView
import refinement.SelectableItem


class RefinementListAdapter :
    ListAdapter<SelectableItem<Facet>, RefinementListViewHolder>(diffUtil),
    RefinementListView<Facet> {

    private lateinit var onClick: (Facet) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RefinementListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.refinement_item, parent, false)

        return RefinementListViewHolder(view)
    }

    override fun onBindViewHolder(holder: RefinementListViewHolder, position: Int) {
        val (facet, selected) = getItem(position)

        holder.bind(facet, selected, View.OnClickListener { onClick(facet) })
    }

    override fun setRefinements(refinements: List<Pair<Facet, Boolean>>) {
        submitList(refinements)
    }

    override fun onClickRefinement(onClick: (Facet) -> Unit) {
        this.onClick = onClick
    }

    companion object {

        private val diffUtil = object : DiffUtil.ItemCallback<SelectableItem<Facet>>() {

            override fun areItemsTheSame(
                oldItem: SelectableItem<Facet>,
                newItem: SelectableItem<Facet>
            ): Boolean {
                return oldItem::class == newItem::class
            }

            override fun areContentsTheSame(
                oldItem: SelectableItem<Facet>,
                newItem: SelectableItem<Facet>
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}