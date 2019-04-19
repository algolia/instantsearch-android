package refinement

import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.search.Facet
import filter.FilterGroupID
import filter.Filters
import filter.add
import refinement.RefinementOperator.And
import refinement.RefinementOperator.Or
import search.SearcherSingleIndex


public fun RefinementFacetsViewModel.connect(
    attribute: Attribute,
    searcher: SearcherSingleIndex,
    operator: RefinementOperator = And,
    groupName: String = attribute.raw
) {
    val groupID = when (operator) {
        And -> FilterGroupID.And(groupName)
        Or -> FilterGroupID.Or(groupName)
    }
    val onFilterStateChange: (Filters) -> Unit = { filters ->
        selections = filters.getFacets(groupID).orEmpty().mapNotNull { it.value.raw as? String }.toSet()
        searcher.search()
    }

    onFilterStateChange(searcher.filterState)
    searcher.filterState.onStateChanged += onFilterStateChange
    searcher.onResponseChange += { response ->
        values = response.facetsOrNull.orEmpty()[attribute].orEmpty()
    }
    onSelectedChanged += { selections ->
        val filters = selections.map { Filter.Facet(attribute, it) }.toSet()

        searcher.filterState.notify {
            clear(groupID)
            add(groupID, filters)
        }
    }
}

fun RefinementFacetsViewModel.connect(
    view: RefinementFacetsView,
    transform: ((List<RefinementFacet>) -> List<RefinementFacet>)? = null
) {

    fun compute(facets: List<Facet>, selections: Set<String>) {
        val selectableItems = facets.map { it to selections.contains(it.value) }

        view.setSelectableItems(transform?.invoke(selectableItems) ?: selectableItems)
    }

    view.onClickItem { facet -> select(facet.value) }
    compute(values, selections)
    onValuesChanged += { facets -> compute(facets, selections) }
    onSelectionsChanged += { selections -> compute(values, selections) }
}