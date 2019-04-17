package com.algolia.instantsearch.sample.refinementList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.algolia.instantsearch.sample.R
import com.algolia.search.model.search.Facet
import refinement.RefinementListView
import refinement.SelectedRefinement


class RefinementListAdapter :
    ListAdapter<SelectedRefinement<Facet>, RefinementListViewHolder>(diffUtil),
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

        private val diffUtil = object : DiffUtil.ItemCallback<SelectedRefinement<Facet>>() {

            override fun areItemsTheSame(
                oldItem: SelectedRefinement<Facet>,
                newItem: SelectedRefinement<Facet>
            ): Boolean {
                return oldItem::class == newItem::class
            }

            override fun areContentsTheSame(
                oldItem: SelectedRefinement<Facet>,
                newItem: SelectedRefinement<Facet>
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}