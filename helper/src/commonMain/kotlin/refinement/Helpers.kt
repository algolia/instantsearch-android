package refinement

import com.algolia.search.filter.FilterBuilder
import com.algolia.search.filter.FilterFacet
import com.algolia.search.filter.Group
import com.algolia.search.filter.GroupAnd
import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Facet
import searcher.SearcherForFacetValue
import searcher.SearcherSingleIndex

internal fun List<Facet>.filter(
    filterBuilder: FilterBuilder,
    attribute: Attribute,
    group: Group
) {
    filterBuilder.apply {
        group.clear(attribute)
        group += map { FilterFacet(attribute, it.name) }
    }
}

internal fun RefinementListViewModel<Facet>.setRefinements(facets: List<Facet>?) {
    if (facets != null) refinements = facets
}

fun RefinementListViewModel<Facet>.connectSearcherSingleIndex(
    searcher: SearcherSingleIndex,
    attribute: Attribute,
    group: Group = GroupAnd(attribute.raw)
) {
    setRefinements(searcher.response?.facets?.get(attribute))
    searcher.responseListeners += { response ->
        setRefinements(response.facets[attribute])
    }
    selectionListeners += { refinements ->
        refinements.filter(searcher.filterBuilder, attribute, group)
        searcher.search()
    }
}

fun RefinementListViewModel<Facet>.connectSearcherForFacetValue(
    searcher: SearcherForFacetValue,
    attribute: Attribute,
    group: Group = GroupAnd(attribute.raw)
) {
    setRefinements(searcher.response?.facets)
    searcher.responseListeners += { response -> setRefinements(response.facets) }
    selectionListeners += { refinements ->
        refinements.filter(searcher.filterBuilder, attribute, group)
        searcher.search()
    }
}