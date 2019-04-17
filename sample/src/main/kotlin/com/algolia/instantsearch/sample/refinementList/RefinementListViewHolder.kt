package com.algolia.instantsearch.sample.refinementList

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.algolia.search.model.search.Facet
import kotlinx.android.synthetic.main.refinement_item.view.*


class RefinementListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(refinement: Facet, selected: Boolean, onClickListener: View.OnClickListener) {
        view.setOnClickListener(onClickListener)
        view.refinementSelectableName.text = refinement.value
        view.refinementSelectableSubtitle.text = refinement.count.toString()
        view.refinementSelectableSubtitle.visibility = View.VISIBLE
        view.refinementSelectableIcon.visibility = if (selected) View.VISIBLE else View.INVISIBLE
    }
}