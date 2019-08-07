package com.algolia.instantsearch.demo.filter.facet

import android.view.View
import android.view.ViewGroup
import com.algolia.instantsearch.core.highlighting.HighlightTokenizer
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.helper.android.filter.facet.FacetListViewHolder
import com.algolia.instantsearch.helper.android.highlighting.toSpannedString
import com.algolia.instantsearch.helper.android.inflate
import com.algolia.search.model.search.Facet
import kotlinx.android.synthetic.main.list_item_selectable.view.*


class FacetListViewHolderImpl(view: View) : FacetListViewHolder(view) {

    override fun bind(facet: Facet, selected: Boolean, onClickListener: View.OnClickListener) {
        view.setOnClickListener(onClickListener)
        view.selectableItemSubtitle.text = facet.count.toString()
        view.selectableItemSubtitle.visibility = View.VISIBLE
        view.selectableItemIcon.visibility = if (selected) View.VISIBLE else View.INVISIBLE
        view.selectableItemName.text = facet.highlightedOrNull?.let {
            HighlightTokenizer(preTag = "<b>", postTag = "</b>")(it).toSpannedString()
        } ?: facet.value
    }

    object Factory : FacetListViewHolder.Factory {

        override fun createViewHolder(parent: ViewGroup): FacetListViewHolder {
            return FacetListViewHolderImpl(parent.inflate(R.layout.list_item_selectable))
        }
    }
}