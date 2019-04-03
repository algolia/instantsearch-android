package refinement

import com.algolia.search.filter.FilterBuilder
import com.algolia.search.filter.FilterFacet
import com.algolia.search.filter.GroupAnd
import com.algolia.search.filter.GroupOr
import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Facet
import refinement.Operator.AND
import searcher.SearcherForFacetValue
import searcher.SearcherSingleIndex

enum class Operator {
    AND,
    OR
}

class SearchFilterState {

    internal val filterBuilder = FilterBuilder()
    internal val filtersListeners = mutableListOf<(FilterBuilder) -> Unit>()


    // region Public API
    fun addFilter(facetFilter: FilterFacet, operator: Operator = AND, preventUpdate: Boolean = false) {
        filterBuilder.apply {
            getGroupFor(facetFilter, operator) += facetFilter
            if (!preventUpdate) updateListeners()
        }
    }

    fun removeFilter(facetFilter: FilterFacet, operator: Operator = AND, preventUpdate: Boolean = false) {
        filterBuilder.apply {
            getGroupFor(facetFilter, operator) -= facetFilter
            if (!preventUpdate) updateListeners()
        }
    }

    fun toggleFilter(facetFilter: FilterFacet, operator: Operator = AND, preventUpdate: Boolean = false) =
        if (filterBuilder.contains(facetFilter)) removeFilter(facetFilter, operator, preventUpdate)
        else addFilter(facetFilter, operator, preventUpdate)

    // endregion

    // region Connectors

    // When the selection changes in the model, update filters: remove obsolete, add new ones
    fun connectRefinementModel(
        model: RefinementListViewModel<Facet>,
        attribute: Attribute,
        operator: Operator = AND
    ) {
        model.selectionListeners += { oldFacets, newFacets ->
            val oldFilters = oldFacets.map { FilterFacet(attribute, it.name) }
            val newFilters = newFacets.map { FilterFacet(attribute, it.name) }
            oldFilters.forEach { removeFilter(it, operator, true) }
            newFilters.forEach { addFilter(it, operator, true) }
            updateListeners()
        }
    }

    fun connectSearcherSingleIndex(searcher: SearcherSingleIndex) {
        filtersListeners += {
            searcher.query.filters = filterBuilder.build()
            searcher.search()
        }
    }

    fun connectSearcherForFacetValue(searcher: SearcherForFacetValue) {
        filtersListeners += {
            searcher.query.filters = filterBuilder.build()
            searcher.search()
        }
    }
    // endregion

    // region Helpers

    private fun getGroupFor(facetFilter: FilterFacet, operator: Operator) =
        if (operator == AND) GroupAnd(facetFilter.attribute.raw) else GroupOr(facetFilter.attribute.raw)

    private fun updateListeners() {
        filtersListeners.forEach { it(filterBuilder) }
    }
    // endregion
}

//data class FacetFilter(val attribute: Attribute, val value: String)