package refinement

import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Facet
import filter.toFilter
import refinement.RefinementMode.And
import refinement.RefinementMode.Or
import search.GroupID
import search.SearcherSingleIndex


public typealias RefinementFacetsViewModel = RefinementListViewModel<Facet>
public typealias RefinementFacetsView = RefinementListView<Facet>

public fun RefinementFacetsViewModel.connectWith(
    searcher: SearcherSingleIndex,
    attribute: Attribute,
    mode: RefinementMode = And,
    view: RefinementFacetsView? = null,
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
    view?.onClickRefinement { facet ->
        val filters = select(facet).map { it.toFilter(attribute) }.toSet()

        searcher.filterState.clear(groupID)
        searcher.filterState.add(groupID, filters)
        searcher.search()
    }
}

public fun RefinementFacetsViewModel.connectWith(presenter: RefinementFacetsPresenter) {
    refinementsListeners += { refinements ->
        presenter.refinements = refinements.map { it to selections.contains(it) }
    }
    selectionsListeners += { selections ->
        presenter.refinements = refinements.map { it to selections.contains(it) }
    }
}