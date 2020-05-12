package com.algolia.instantsearch.helper.relateditems.internal.extensions

import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.groupAnd
import com.algolia.instantsearch.helper.filter.state.groupOr
import com.algolia.instantsearch.helper.relateditems.MatchingPattern
import com.algolia.instantsearch.helper.relateditems.internal.OptionalFilter
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.FilterGroupsConverter

internal fun <T> List<MatchingPattern<T>>.toOptionalFilters(hit: T): List<List<String>>? {
    val filterState = FilterState()
    forEach { filterState.addMatchingPattern(hit, it) }
    return FilterGroupsConverter.Legacy.Facet(filterState.toFilterFacetGroup()).unquote()
}

/**
 * Create an [OptionalFilter] from a [MatchingPattern].
 */
internal fun <T> MatchingPattern<T>.toOptionalFilter(hit: T): OptionalFilter {
    return when (val property = property.get(hit)) {
        is Iterable<*> -> {
            val groupOr = groupOr()
            val list = property.map { value -> Filter.Facet(attribute, value.toString(), score) }.toTypedArray()
            OptionalFilter(
                groupOr,
                list
            )
        }
        else -> OptionalFilter(
            groupAnd(),
            arrayOf(Filter.Facet(attribute, property.toString(), score))
        )
    }
}
