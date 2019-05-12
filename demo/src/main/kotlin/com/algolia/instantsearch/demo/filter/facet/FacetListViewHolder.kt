package com.algolia.instantsearch.demo.filter.facet

import android.text.Html
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.algolia.search.model.search.Facet
import kotlinx.android.synthetic.main.list_item_selectable.view.*


class FacetListViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(facet: Facet, selected: Boolean, onClickListener: View.OnClickListener) {
        view.setOnClickListener(onClickListener)
        view.selectableItemName.text = facet.highlightedOrNull?.let {
            Html.fromHtml(it, Html.FROM_HTML_MODE_COMPACT)
        } ?: facet.value
        view.selectableItemSubtitle.text = facet.count.toString()
        view.selectableItemSubtitle.visibility = View.VISIBLE
        view.selectableItemIcon.visibility = if (selected) View.VISIBLE else View.INVISIBLE
    }
}