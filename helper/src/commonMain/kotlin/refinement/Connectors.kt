package refinement

import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Facet
import filter.toFilter
import refinement.RefinementMode.And
import refinement.RefinementMode.Or
import search.GroupID
import search.SearcherSingleIndex


typealias RefinementFacetsViewModel = RefinementListViewModel<Facet>

public fun RefinementFacetsViewModel.connectWith(
    searcher: SearcherSingleIndex,
    attribute: Attribute,
    mode: RefinementMode = And,
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

        selections = refinements.filter { refinement -> filters.any { it.raw == refinement.value } }
    }
    selectionListeners += { facets ->
        val filters = facets.map { it.toFilter(attribute) }.toSet()

        searcher.filterState.add(groupID, filters)
    }
}

public fun RefinementFacetsViewModel.connectWith(presenter: RefinementFacetsPresenter) {
    refinementListeners += { refinements ->
        presenter.refinements = refinements.map { it to selections.contains(it) }
    }
    selectionListeners += { selections ->
        presenter.refinements = refinements.map { it to selections.contains(it) }
    }
}


