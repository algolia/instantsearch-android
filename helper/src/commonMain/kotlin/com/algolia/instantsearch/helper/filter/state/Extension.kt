package com.algolia.instantsearch.helper.filter.state

import com.algolia.instantsearch.core.number.range.Range
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.FilterGroup
import com.algolia.search.model.search.Facet


public fun Filters.toFilterGroups(): List<FilterGroup<*>> {
    return getFacetGroups().map { (key, value) ->
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
    }
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

internal fun Facet.toFilter(attribute: Attribute): Filter.Facet {
    return Filter.Facet(attribute, value)
}

public fun <T> Range<T>.toFilterNumeric(attribute: Attribute): Filter.Numeric where T : Number, T : Comparable<T> {
    return Filter.Numeric(attribute, false, Filter.Numeric.Value.Range(min, max))
}