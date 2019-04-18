package refinement

import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
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
        selections = filters.getFacets(groupID).orEmpty().mapNotNull { it.value.raw as? String }
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

fun RefinementFacetsViewModel.connect(presenter: RefinementFacetsPresenter) {
    onValuesChange += { facets ->
        presenter.values = facets.map { it to selections.contains(it.value) }
    }
    onSelectionsChange += { selections ->
        presenter.values = values.map { it to selections.contains(it.value) }
    }
}

fun RefinementFacetsViewModel.connect(view: RefinementFacetsView) {
    view.onClickItem { facet -> select(facet.value) }
}

fun RefinementFacetsPresenter.connect(view: RefinementFacetsView) {
    onValuesChange += { view.setSelectableItems(it) }
}