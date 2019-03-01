package searcher

import com.algolia.search.model.Attribute
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.response.ResponseSearchForFacetValue
import com.algolia.search.model.search.Facet
import com.algolia.search.query.FilterFacet
import com.algolia.search.query.GroupOr
import refinement.RefinementModel


fun RefinementModel<Facet>.connectSearcherSingleQuery(
    searcher: SearcherSingleQuery,
    attribute: Attribute,
    variant: String = attribute.raw
) {
    val group = GroupOr(variant)

    searcher.listeners += { response: ResponseSearch ->
        response.facets[attribute]?.let {
            refinements = it
        }
    }
    selectedListeners += { refinements ->
        searcher.query.filterBuilder.apply {
            group.clear(attribute)
            group += refinements.map { FilterFacet(attribute, it.name) }
        }
        searcher.search()
    }
}

fun RefinementModel<Facet>.connectSearcherForFacetValue(
    searcher: SearcherForFacetValue
) {
    searcher.listeners += { response: ResponseSearchForFacetValue ->
        refinements = response.facets
    }
}
