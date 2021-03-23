package com.algolia.instantsearch.helper.filter.facet.dynamic

import com.algolia.search.model.Attribute
import com.algolia.search.model.rule.AttributedFacets
import com.algolia.search.model.search.Facet

public interface DynamicFacetView {

    public fun commit(facetOrder: List<AttributedFacets>)

    public fun commit(selections: FacetSelections)

    public var didSelect: ((Attribute, Facet) -> Unit)?
}
