package refinement.facet

import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.search.Facet
import filter.FilterGroupID
import filter.Filters
import filter.add
import refinement.RefinementOperator
import refinement.RefinementOperator.And
import refinement.RefinementOperator.Or
import search.SearcherSingleIndex


public fun RefinementFacetsViewModel.connectSearcher(
    attribute: Attribute,
    searcher: SearcherSingleIndex,
    operator: RefinementOperator = Or,
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
        val disjunctiveFacets = response.disjunctiveFacetsOrNull?.get(attribute)

        items = disjunctiveFacets ?: response.facetsOrNull.orEmpty()[attribute].orEmpty()
    }
    onSelectionsComputed += { selections ->
        val filters = selections.map { Filter.Facet(attribute, it) }.toSet()

        searcher.filterState.notify {
            clear(groupID)
            add(groupID, filters)
        }
    }
}

fun RefinementFacetsViewModel.connectView(
    view: RefinementFacetsView,
    presenter: ((List<RefinementFacet>) -> List<RefinementFacet>)? = null
) {

    fun setSelectableItems(facets: List<Facet>, selections: Set<String>) {
        val selectableItems = facets.map { Pair(it, selections.contains(it.value)) }

        view.setSelectableItems(presenter?.invoke(selectableItems) ?: selectableItems)
    }

    setSelectableItems(items, selections)
    view.onClickItem { facet -> select(facet.value) }
    onValuesChanged += { items -> setSelectableItems(items, selections) }
    onSelectionsChanged += { selections -> setSelectableItems(items, selections) }
}