package refinement.facet

import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.search.Facet
import filter.FilterGroupID
import filter.Filters
import filter.add
import filter.getValue
import search.SearcherSingleIndex
import search.addFacet


public fun RefinementFacetsViewModel.connectSearcher(
    attribute: Attribute,
    searcher: SearcherSingleIndex,
    groupID: FilterGroupID = FilterGroupID.Or(attribute)
) {
    fun onSelectionsComputedThenUpdateFilterState() {
        onSelectionsComputed += { selections ->
            val filters = selections.map { Filter.Facet(attribute, it) }.toSet()

            searcher.filterState.notify {
                clear(groupID)
                add(groupID, filters)
            }
        }
    }

    fun onFilterStateChangedThenUpdateSelections() {
        val onChange: (Filters) -> Unit = { filters ->
            selections = filters.getFacetFilters(groupID)
                .orEmpty()
                .map { it.getValue() }
                .toSet()
        }

        onChange(searcher.filterState)
        searcher.filterState.onChange += onChange
    }

    fun onResponseChangedThenUpdateItems() {
        searcher.onResponseChanged += { response ->
            val disjunctiveFacets = response.disjunctiveFacetsOrNull?.get(attribute)

            items = disjunctiveFacets ?: response.facetsOrNull.orEmpty()[attribute].orEmpty()
        }
    }

    searcher.query.addFacet(attribute)
    onSelectionsComputedThenUpdateFilterState()
    onFilterStateChangedThenUpdateSelections()
    onResponseChangedThenUpdateItems()
}

// TODO Demo persistent selection
public fun RefinementFacetsViewModel.connectView(
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

    fun setSelectableItems(facets: List<Facet>, selections: Set<String>) {
        val selectableItems = facets.toRefinementFacets(selections)

        view.setSelectableItems(presenter?.invoke(selectableItems) ?: selectableItems)
    }

    setSelectableItems(items, selections)
    view.onClick = { facet -> computeSelections(facet.value) }
    onItemsChanged += { items -> setSelectableItems(items, selections) }
    onSelectionsChanged += { selections -> setSelectableItems(items, selections) }
}