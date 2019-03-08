package searcher

import com.algolia.search.filter.FilterFacet
import com.algolia.search.filter.GroupOr
import com.algolia.search.model.Attribute
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.response.ResponseSearchForFacetValue
import com.algolia.search.model.search.Facet
import model.Variant
import refinement.RefinementModel


fun RefinementModel<Facet>.connectSearcherSingleIndex(
    searcher: SearcherSingleIndex,
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
