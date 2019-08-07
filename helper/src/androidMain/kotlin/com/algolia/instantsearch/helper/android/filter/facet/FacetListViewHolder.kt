package com.algolia.instantsearch.helper.android.filter.facet

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.algolia.search.model.search.Facet


public abstract class FacetListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    public abstract fun bind(facet: Facet, selected: Boolean, onClickListener: View.OnClickListener)

    interface Factory {

        fun createViewHolder(parent: ViewGroup): FacetListViewHolder
    }
}