package com.algolia.instantsearch.android.filter.facet

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.filter.Facet

public abstract class FacetListViewHolder(public val view: View) : RecyclerView.ViewHolder(view) {

    public abstract fun bind(facet: Facet, selected: Boolean, onClickListener: View.OnClickListener)

    public interface Factory {

        public fun createViewHolder(parent: ViewGroup): FacetListViewHolder
    }
}
