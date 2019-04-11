package refinement

import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.search.Facet
import refinement.RefinementMode.And
import refinement.RefinementMode.Or
import search.Group
import search.SearcherSingleIndex
import search.getFilters


fun RefinementListViewModel<Facet>.connectWith(
    searcher: SearcherSingleIndex,
    mode: RefinementMode,
    attribute: Attribute,
    groupName: String = attribute.raw
) {
    val group = when (mode) {
        And -> Group.And.Facet(groupName)
        Or -> Group.Or.Facet(groupName)
    }

    searcher.responseListeners += { response ->
        response.facets[attribute]?.let { refinements = it }
    }
    searcher.filterState.stateListeners += { state ->
        val filters = state.getFilters(group).orEmpty().map { it.value }

        selected = refinements.filter { refinement -> filters.any { it.raw == refinement.value } }
    }
    selectionListeners += { facets ->
        val filters = facets.map { it.toFilter(attribute) }.toSet()

        searcher.filterState.replace(group, filters)
    }
}

fun Facet.toFilter(attribute: Attribute): Filter.Facet = Filter.Facet(attribute, this.value)


