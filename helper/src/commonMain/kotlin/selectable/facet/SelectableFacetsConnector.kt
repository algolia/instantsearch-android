package selectable.facet

import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.search.Facet
import filter.FilterGroupID
import filter.Filters
import filter.add
import filter.getValue
import search.SearcherSingleIndex
import search.addFacet


public fun SelectableFacetsViewModel.connectSearcher(
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
public fun SelectableFacetsViewModel.connectView(
    view: SelectableFacetsView,
    //  persistentSelection: Boolean = true,
    presenter: ((List<SelectableFacet>) -> List<SelectableFacet>)? = null
) {
    fun List<Facet>.toSelectableFacets(selections: Set<String>): List<SelectableFacet> {
        return map { Pair(it, selections.contains(it.value)) }
//        val persistentFacets = if (persistentSelection) {
//            selections
//                .filter { selection -> facets.any { it.first.value ==  selection } }
//                .map { Pair(Facet(it, 0), true) }
//        } else listOf()
//        return persistentFacets + facets
    }

    fun setSelectableItems(facets: List<Facet>, selections: Set<String>) {
        val selectableItems = facets.toSelectableFacets(selections)

        view.setSelectableItems(presenter?.invoke(selectableItems) ?: selectableItems)
    }

    setSelectableItems(items, selections)
    view.onClick = { facet -> computeSelections(facet.value) }
    onItemsChanged += { items -> setSelectableItems(items, selections) }
    onSelectionsChanged += { selections -> setSelectableItems(items, selections) }
}