package com.algolia.instantsearch.showcase.filter.facet

import android.view.View
import android.view.ViewGroup
import com.algolia.instantsearch.core.highlighting.HighlightTokenizer
import com.algolia.instantsearch.android.filter.facet.FacetListViewHolder
import com.algolia.instantsearch.android.highlighting.toSpannedString
import com.algolia.instantsearch.showcase.databinding.ListItemSelectableBinding
import com.algolia.instantsearch.showcase.layoutInflater
import com.algolia.search.model.search.Facet

class FacetListViewHolderImpl(private val binding: ListItemSelectableBinding) :
    FacetListViewHolder(binding.root) {

    override fun bind(facet: Facet, selected: Boolean, onClickListener: View.OnClickListener) {
        binding.root.setOnClickListener(onClickListener)
        binding.selectableItemSubtitle.text = facet.count.toString()
        binding.selectableItemSubtitle.visibility = View.VISIBLE
        binding.selectableItemIcon.visibility = if (selected) View.VISIBLE else View.INVISIBLE
        binding.selectableItemName.text = facet.highlightedOrNull?.let {
            HighlightTokenizer(preTag = "<b>", postTag = "</b>")(it).toSpannedString()
        } ?: facet.value
    }

    object Factory : FacetListViewHolder.Factory {

        override fun createViewHolder(parent: ViewGroup): FacetListViewHolder {
            return FacetListViewHolderImpl(
                ListItemSelectableBinding.inflate(parent.layoutInflater, parent, false)
            )
        }
    }
}
