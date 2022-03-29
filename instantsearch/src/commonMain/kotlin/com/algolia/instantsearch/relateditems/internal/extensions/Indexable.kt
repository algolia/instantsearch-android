package com.algolia.instantsearch.relateditems.internal.extensions

import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.FilterGroup
import com.algolia.search.model.filter.FilterGroupsConverter
import com.algolia.search.model.indexing.Indexable

internal fun Indexable.toFacetFilter(isNegated: Boolean = false): List<List<String>> {
    val filter = Filter.Facet(Attribute("objectID"), objectID.toString(), isNegated = isNegated)
    val filterGroups = setOf<FilterGroup<Filter.Facet>>(FilterGroup.And.Facet(filter))
    return FilterGroupsConverter.Legacy.Facet(filterGroups).unquote()
}
