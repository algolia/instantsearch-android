package com.algolia.instantsearch.examples.android.guides.gettingstarted

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.algolia.client.model.search.FacetHits
import com.algolia.instantsearch.android.filter.facet.FacetListViewHolder
import com.algolia.instantsearch.android.inflate
import com.algolia.instantsearch.examples.android.R

class MyFacetListViewHolder(view: View) : FacetListViewHolder(view) {

    override fun bind(facet: FacetHits, selected: Boolean, onClickListener: View.OnClickListener) {
        view.setOnClickListener(onClickListener)
        val facetCount = view.findViewById<TextView>(R.id.facetCount)
        facetCount.text = facet.count.toString()
        facetCount.visibility = View.VISIBLE
        view.findViewById<ImageView>(R.id.icon).visibility = if (selected) View.VISIBLE else View.INVISIBLE
        view.findViewById<TextView>(R.id.facetName).text = facet.value
    }

    object Factory : FacetListViewHolder.Factory {

        override fun createViewHolder(parent: ViewGroup): FacetListViewHolder {
            return MyFacetListViewHolder(parent.inflate(R.layout.list_facet_selectable))
        }
    }
}
