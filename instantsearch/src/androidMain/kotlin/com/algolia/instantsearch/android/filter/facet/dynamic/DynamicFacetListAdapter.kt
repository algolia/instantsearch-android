package com.algolia.instantsearch.android.filter.facet.dynamic

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.algolia.instantsearch.filter.facet.dynamic.AttributedFacets
import com.algolia.instantsearch.filter.facet.dynamic.DynamicFacetListView
import com.algolia.instantsearch.filter.facet.dynamic.SelectionsPerAttribute
import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Facet

/**
 * [DynamicFacetListView] implementation presenting ordered facets and ordered facet values.
 *
 * @param factory dynamic facet list view holder factory
 */
public class DynamicFacetListAdapter(
    private val factory: DynamicFacetListViewHolder.Factory,
) : ListAdapter<DynamicFacetModel, DynamicFacetListViewHolder<out DynamicFacetModel>>(DiffUtil), DynamicFacetListView {

    override var didSelect: ((Attribute, Facet) -> Unit)? = null
    private var facetSelections: SelectionsPerAttribute = emptyMap()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DynamicFacetListViewHolder<out DynamicFacetModel> {
        return factory.createViewHolder(parent, DynamicFacetListViewHolder.ViewType.values()[viewType])
    }

    override fun onBindViewHolder(holder: DynamicFacetListViewHolder<out DynamicFacetModel>, position: Int) {
        @Suppress("UNCHECKED_CAST")
        when (val item = getItem(position)) {
            is DynamicFacetModel.Header -> (holder as? DynamicFacetListHeaderViewHolder)?.bind(item)
            is DynamicFacetModel.Item -> (holder as? DynamicFacetListItemViewHolder)?.bind(item) {
                didSelect?.let { it(item.attribute, item.facet) }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when (item) {
            is DynamicFacetModel.Header -> DynamicFacetListViewHolder.ViewType.Header
            is DynamicFacetModel.Item -> DynamicFacetListViewHolder.ViewType.Item
        }.ordinal
    }

    override fun setOrderedFacets(facetOrder: List<AttributedFacets>) {
        val list = mutableListOf<DynamicFacetModel>()
        facetOrder.onEach { (attribute, facets) ->
            list += DynamicFacetModel.Header(attribute)
            facets.onEach { facet ->
                val selected = isSelected(attribute, facet)
                list += DynamicFacetModel.Item(attribute, facet, selected)
            }
        }
        submitList(list)
    }

    override fun setSelections(selections: SelectionsPerAttribute) {
        if (facetSelections == selections) return
        facetSelections = selections
        updateListItems()
    }

    private fun updateListItems() {
        val newList = currentList.map { facetItem ->
            when (facetItem) {
                is DynamicFacetModel.Header -> facetItem
                is DynamicFacetModel.Item -> {
                    val selected = isSelected(facetItem.attribute, facetItem.facet)
                    if (selected == facetItem.selected) facetItem else facetItem.copy(selected = selected)
                }
            }
        }
        if (currentList != newList) submitList(newList)
    }

    private fun isSelected(attribute: Attribute, facet: Facet): Boolean {
        return facetSelections[attribute]?.contains(facet.value) == true
    }

    public companion object {

        private val DiffUtil = object : DiffUtil.ItemCallback<DynamicFacetModel>() {

            override fun areItemsTheSame(
                oldItemDynamic: DynamicFacetModel,
                newItemDynamic: DynamicFacetModel
            ): Boolean {
                return oldItemDynamic === newItemDynamic
            }

            override fun areContentsTheSame(
                oldItemDynamic: DynamicFacetModel,
                newItemDynamic: DynamicFacetModel
            ): Boolean {
                return oldItemDynamic == newItemDynamic
            }
        }
    }
}
