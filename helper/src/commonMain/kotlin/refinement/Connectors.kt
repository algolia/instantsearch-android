package refinement

import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.search.Facet
import refinement.RefinementMode.And
import refinement.RefinementMode.Or
import search.GroupID
import search.SearcherSingleIndex


typealias RefinementFacetsViewModel = RefinementListViewModel<Facet>

fun RefinementFacetsViewModel.connectWith(
    searcher: SearcherSingleIndex,
    mode: RefinementMode,
    attribute: Attribute,
    groupName: String = attribute.raw
) {
    val groupID = when (mode) {
        And -> GroupID.And(groupName)
        Or -> GroupID.Or(groupName)
    }

    searcher.responseListeners += { response ->
        response.facets[attribute]?.let { refinements = it }
    }
    searcher.filterState.listeners += { state ->
        val filters = state.facet[groupID].orEmpty().map { it.value }

        selected = refinements.filter { refinement -> filters.any { it.raw == refinement.value } }
    }
    selectionListeners += { facets ->
        val filters = facets.map { it.toFilter(attribute) }.toSet()

        searcher.filterState.add(groupID, filters)
    }
}

fun Facet.toFilter(attribute: Attribute): Filter.Facet = Filter.Facet(attribute, this.value)


