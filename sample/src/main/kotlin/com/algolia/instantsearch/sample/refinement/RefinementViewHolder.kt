package com.algolia.instantsearch.sample.refinement

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.algolia.search.model.search.Facet
import kotlinx.android.synthetic.main.refinement_item.view.*


class RefinementViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(refinement: Facet, selected: Boolean, onClickListener: View.OnClickListener) {
        view.setOnClickListener(onClickListener)
        view.refinementSelectableName.text = refinement.value
        view.refinementSelectableIcon.visibility = if (selected) View.VISIBLE else View.INVISIBLE
    }
}