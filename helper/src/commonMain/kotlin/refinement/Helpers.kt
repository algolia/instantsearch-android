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


// Links model to searcher, updating model -> view on new results
fun RefinementListViewModel<Facet>.connectSearcherSingleIndex(
    searcher: SearcherSingleIndex,
    attribute: Attribute
) {
    searcher.responseListeners += {response ->
        response.facetsOrNull?.get(attribute)?.let { data = it }
    }
}

fun RefinementListViewModel<Facet>.connectSearcherForFacetValue(
    searcher: SearcherForFacetValue,
    attribute: Attribute,
    group: Group = GroupAnd(attribute.raw)
) {
    searcher.responseListeners += { response -> data = response.facets }
    selectionListeners += { _, refinements ->
        val filterBuilder = FilterBuilder()
        refinements.filter(filterBuilder, attribute, group)
        searcher.query?.filters = filterBuilder.build()
        searcher.search()
    }
}