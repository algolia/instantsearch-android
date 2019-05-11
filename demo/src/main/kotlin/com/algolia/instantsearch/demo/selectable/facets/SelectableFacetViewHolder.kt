package com.algolia.instantsearch.demo.selectable.facets

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.algolia.search.model.search.Facet
import kotlinx.android.synthetic.main.selectable_item.view.*


class SelectableFacetViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(facet: Facet, selected: Boolean, onClickListener: View.OnClickListener) {
        view.setOnClickListener(onClickListener)
        view.selectableItemName.text = facet.value
        view.selectableItemSubtitle.text = facet.count.toString()
        view.selectableItemSubtitle.visibility = View.VISIBLE
        view.selectableItemIcon.visibility = if (selected) View.VISIBLE else View.INVISIBLE
    }
}