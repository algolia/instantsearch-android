package com.algolia.instantsearch.relateditems.internal.extensions

import com.algolia.instantsearch.migration2to3.Attribute
import com.algolia.instantsearch.migration2to3.Filter
import com.algolia.instantsearch.migration2to3.FilterGroup


internal fun Indexable.toFacetFilter(isNegated: Boolean = false): List<List<String>> {
    val filter = Filter.Facet(Attribute("objectID"), objectID.toString(), isNegated = isNegated)
    val filterGroups = setOf<FilterGroup<Filter.Facet>>(FilterGroup.And.Facet(filter))
    return FilterGroupsConverter.Legacy.Facet(filterGroups).unquote()
}
