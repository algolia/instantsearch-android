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


public fun RefinementFacetsViewModel.connect(
    attribute: Attribute,
    searcher: SearcherSingleIndex,
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
        val filters = state.getFacets(groupID).orEmpty().map { it.value }

        selections = refinements.filter { refinement -> filters.any { it.raw == refinement.value } }
    }
    selectedListeners += { facets ->
        val currentFilters = selections.map { it.toFilter(attribute) }.toSet()
        val filters = facets.map { it.toFilter(attribute) }.toSet()

        searcher.filterState.transaction {
            remove(groupID, currentFilters)
            add(groupID, filters)
        }
        searcher.search()
    }
}

fun RefinementFacetsViewModel.connect(presenter: RefinementFacetsPresenter) {
    refinementsListeners += { refinements ->
        presenter.refinements = refinements.map { it to selections.contains(it) }
    }
    selectionsListeners += { selections ->
        presenter.refinements = refinements.map { it to selections.contains(it) }
    }
}

fun RefinementFacetsViewModel.connect(view: RefinementFacetsView) {
    view.onClickRefinement(::select)
}

fun RefinementFacetsPresenter.connect(view: RefinementFacetsView) {
    listeners += { view.setRefinements(it) }
}