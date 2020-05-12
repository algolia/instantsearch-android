package com.algolia.instantsearch.helper.relateditems.internal.extensions

import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.FilterGroup
import com.algolia.search.model.filter.FilterGroupsConverter
import com.algolia.search.model.indexing.Indexable

internal fun Indexable.toNegatedFacetFilter(): List<List<String>> {
    val filter = Filter.Facet(Attribute("objectID"), objectID.toString(), isNegated = true)
    val filterGroups = setOf<FilterGroup<Filter.Facet>>(FilterGroup.And.Facet(filter))
    return FilterGroupsConverter.Legacy.Facet(filterGroups).unquote()
}
