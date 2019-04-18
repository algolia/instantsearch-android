package com.algolia.instantsearch.sample.refinementList

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.algolia.search.model.search.Facet
import kotlinx.android.synthetic.main.refinement_item.view.*


class RefinementFacetViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(facet: Facet, selected: Boolean, onClickListener: View.OnClickListener) {
        view.setOnClickListener(onClickListener)
        view.refinementSelectableName.text = facet.value
        view.refinementSelectableSubtitle.text = facet.count.toString()
        view.refinementSelectableSubtitle.visibility = View.VISIBLE
        view.refinementSelectableIcon.visibility = if (selected) View.VISIBLE else View.INVISIBLE
    }
}