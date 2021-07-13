package com.algolia.instantsearch.helper.android.filter.facet.dynamic

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Dynamic facet list view holder.
 */
public abstract class DynamicFacetListViewHolder(public val view: View) : RecyclerView.ViewHolder(view) {

    /**
     * Binds a [DynamicFacetModel] to a view.
     */
    public abstract fun bind(item: DynamicFacetModel, onClick: View.OnClickListener? = null)

    /**
     * Dynamic facet list view holder factory.
     */
    public interface Factory {

        /**
         * Creates a [DynamicFacetListViewHolder] instance.
         *
         * @param parent the [ViewGroup] into which the new View will be added
         * @param viewType the view type of the new View
         * @returns a new view holder that holds a View of the given view type
         */
        public fun createViewHolder(parent: ViewGroup, viewType: ViewType): DynamicFacetListViewHolder

    }

    /**
     * Dynamic facet view type.
     */
    public enum class ViewType {

        /** An attribute view */
        Header,

        /** An attributed facet view */
        Item
    }
}
