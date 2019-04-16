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


public fun widgetRefinement(
    attribute: Attribute,
    searcher: SearcherSingleIndex,
    model: RefinementFacetsViewModel,
    presenter: RefinementFacetsPresenter? = null,
    view: RefinementFacetsView? = null,
    mode: RefinementMode = And,
    groupName: String = attribute.raw
) {
    val groupID = when (mode) {
        And -> GroupID.And(groupName)
        Or -> GroupID.Or(groupName)
    }

    searcher.responseListeners += { response ->
        response.facets[attribute]?.let { model.refinements = it }
    }
    searcher.filterState.listeners += { state ->
        val filters = state.facet[groupID].orEmpty().map { it.value }

        model.selections = model.refinements.filter { refinement -> filters.any { it.raw == refinement.value } }
    }
    view?.onClickRefinement { facet ->
        val filters = model.select(facet).map { it.toFilter(attribute) }.toSet()

        searcher.filterState.clear(groupID)
        searcher.filterState.add(groupID, filters)
        searcher.search()
    }
    presenter?.apply {
        model.refinementsListeners += { refinements ->
            this.refinements = refinements.map { it to model.selections.contains(it) }
        }
        model.selectionsListeners += { selections ->
            refinements = model.refinements.map { it to selections.contains(it) }
        }
        if (view != null) listeners += { view.setRefinements(it) }
    }
}