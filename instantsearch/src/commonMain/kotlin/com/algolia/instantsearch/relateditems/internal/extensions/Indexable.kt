package com.algolia.instantsearch.relateditems.internal.extensions

import com.algolia.instantsearch.core.Indexable
import com.algolia.instantsearch.filter.Filter
import com.algolia.instantsearch.filter.FilterGroup
import com.algolia.instantsearch.filter.FilterGroupsConverter

internal fun Indexable.toFacetFilter(isNegated: Boolean = false): List<List<String>> {
    val filter = Filter.Facet("objectID", objectID, isNegated = isNegated)
    val filterGroups = setOf<FilterGroup<Filter.Facet>>(FilterGroup.And.Facet(filter))
    return FilterGroupsConverter.Legacy.Facet(filterGroups).unquote()
}
