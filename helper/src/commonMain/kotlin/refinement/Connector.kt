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
import selection.SelectableItem


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
    searcher.filterState.onStateChange += onFilterStateChange
    searcher.onResponseChange += { response ->
        values = response.facetsOrNull.orEmpty()[attribute].orEmpty()
    }
    onSelectedChange += { selections ->
        val filters = selections.map { Filter.Facet(attribute, it) }.toSet()

        searcher.filterState.notify {
            clear(groupID)
            add(groupID, filters)
        }
    }
}

fun RefinementFacetsViewModel.connect(
    view: RefinementFacetsView,
    transform: ((List<SelectableItem<Facet>>) -> List<SelectableItem<Facet>>)? = null
) {

    fun compute(facets: List<Facet>, selections: Set<String>) {
        val selectableItems = facets.map { it to selections.contains(it.value) }

        view.setSelectableItems(transform?.invoke(selectableItems) ?: selectableItems)
    }

    view.onClickItem { facet -> select(facet.value) }
    compute(values, selections)
    onValuesChange += { facets -> compute(facets, selections) }
    onSelectionsChange += { selections -> compute(values, selections) }
}