package refinement

import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import filter.Filters
import filter.add
import refinement.RefinementMode.And
import refinement.RefinementMode.Or
import search.GroupID
import search.SearcherSingleIndex


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
    val filterStateListener: (Filters) -> Unit = { state ->
        selections = state.getFacets(groupID).orEmpty().map { it.value.raw as String }
        searcher.search()
    }

    filterStateListener(searcher.filterState)
    searcher.filterState.listeners += filterStateListener
    searcher.responseListeners += { response ->
        values = response.facetsOrNull.orEmpty()[attribute].orEmpty()
    }
    selectedListeners += { selections ->
        val filters = selections.map { Filter.Facet(attribute, it) }.toSet()

        searcher.filterState.notify {
            clear(groupID)
            add(groupID, filters)
        }
    }
}

fun RefinementFacetsViewModel.connect(presenter: RefinementFacetsPresenter) {
    refinementsListeners += { refinements ->
        presenter.refinements = refinements.map { it to selections.contains(it.value) }
    }
    selectionsListeners += { selections ->
        presenter.refinements = values.map { it to selections.contains(it.value) }
    }
}

fun RefinementFacetsViewModel.connect(view: RefinementFacetsView) {
    view.onClickRefinement { facet -> select(facet.value) }
}

fun RefinementFacetsPresenter.connect(view: RefinementFacetsView) {
    listeners += { view.setRefinements(it) }
}