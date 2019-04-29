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

    fun whenSelectionsComputedThenUpdateFilterState(groupID: FilterGroupID) {
        onSelectionsComputed += { selections ->
            val filters = selections.map { Filter.Facet(attribute, it) }.toSet()

            searcher.filterState.notify {
                clear(groupID)
                add(groupID, filters)
            }
        }
    }

    fun whenFilterStateChangedThenUpdateSelections(groupID: FilterGroupID) {
        val onChange: (Filters) -> Unit = { filters ->
            selections = filters.getFacets(groupID).orEmpty().mapNotNull { it.value.raw as? String }.toSet()
        }

        onChange(searcher.filterState)
        searcher.filterState.onChange += onChange
    }

    fun whenOnResponseChangedThenUpdateItems() {
        searcher.onResponseChanged += { response ->
            val disjunctiveFacets = response.disjunctiveFacetsOrNull?.get(attribute)

            items = disjunctiveFacets ?: response.facetsOrNull.orEmpty()[attribute].orEmpty()
        }
    }

    val groupID = when (operator) {
        And -> FilterGroupID.And(groupName)
        Or -> FilterGroupID.Or(groupName)
    }

    whenSelectionsComputedThenUpdateFilterState(groupID)
    whenFilterStateChangedThenUpdateSelections(groupID)
    whenOnResponseChangedThenUpdateItems()
}

// TODO Demo persistent selection
fun RefinementFacetsViewModel.connectView(
    view: RefinementFacetsView,
   //  persistentSelection: Boolean = true,
    presenter: ((List<RefinementFacet>) -> List<RefinementFacet>)? = null
) {
    fun List<Facet>.toRefinementFacets(selections: Set<String>): List<RefinementFacet> {
        return map { Pair(it, selections.contains(it.value)) }
//        val persistentFacets = if (persistentSelection) {
//            selections
//                .filter { selection -> facets.any { it.first.value ==  selection } }
//                .map { Pair(Facet(it, 0), true) }
//        } else listOf()
//        return persistentFacets + facets
    }

    fun assignSelectableItems(facets: List<Facet>, selections: Set<String>) {
        val selectableItems = facets.toRefinementFacets(selections)

        view.setSelectableItems(presenter?.invoke(selectableItems) ?: selectableItems)
    }

    assignSelectableItems(items, selections)
    view.onClick = { facet -> selectItem(facet.value) }
    onItemsChanged += { items -> assignSelectableItems(items, selections) }
    onSelectionsChanged += { selections -> assignSelectableItems(items, selections) }
}