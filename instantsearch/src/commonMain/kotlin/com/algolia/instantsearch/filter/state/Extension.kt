package com.algolia.instantsearch.filter.state

import com.algolia.instantsearch.core.number.range.Range
import com.algolia.instantsearch.filter.Facet
import com.algolia.instantsearch.filter.Filter
import com.algolia.instantsearch.filter.FilterGroup


public fun Filters.toFilterGroups(): Set<FilterGroup<*>> {
    return (
        getFacetGroups().map { (key, value) ->
            when (key.operator) {
                FilterOperator.And -> FilterGroup.And.Facet(value, key.name)
                FilterOperator.Or -> FilterGroup.Or.Facet(value, key.name)
            }
        } + getTagGroups().map { (key, value) ->
            when (key.operator) {
                FilterOperator.And -> FilterGroup.And.Tag(value, key.name)
                FilterOperator.Or -> FilterGroup.Or.Tag(value, key.name)
            }
        } + getNumericGroups().map { (key, value) ->
            when (key.operator) {
                FilterOperator.And -> FilterGroup.And.Numeric(value, key.name)
                FilterOperator.Or -> FilterGroup.Or.Numeric(value, key.name)
            }
        } + getHierarchicalGroups().map { (key, value) ->
            FilterGroup.And.Hierarchical(
                filters = setOf(value.filter),
                path = value.path,
                attributes = value.attributes,
                name = key
            )
        }
        ).toSet()
}

public inline fun <reified T : Filter> MutableFilters.add(groupID: FilterGroupID, filters: Set<T>) {
    add(groupID, *filters.toTypedArray())
}

public inline fun <reified T : Filter> MutableFilters.remove(groupID: FilterGroupID, filters: Set<T>) {
    remove(groupID, *filters.toTypedArray())
}

public fun Filter.Facet.getValue(): String {
    return when (val value = value) {
        is Filter.Facet.Value.String -> value.raw
        is Filter.Facet.Value.Boolean -> value.raw.toString()
        is Filter.Facet.Value.Number -> value.raw.toString()
    }
}

public fun <T> Range<T>.toFilterNumeric(attribute: String): Filter.Numeric where T : Number, T : Comparable<T> {
    return Filter.Numeric(attribute, false, Filter.Numeric.Value.Range(min, max))
}

public fun Facet.toFilter(attribute: String): Filter.Facet {
    return Filter.Facet(attribute, value)
}
