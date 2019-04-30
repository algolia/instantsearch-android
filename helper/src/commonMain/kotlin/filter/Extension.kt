package filter

import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.FilterGroup
import com.algolia.search.model.search.Facet


public fun Filters.toFilterGroups(): List<FilterGroup<*>> {
    return getFacetGroups().map { (key, value) ->
        when (key) {
            is FilterGroupID.And -> FilterGroup.And.Facet(value, key.name)
            is FilterGroupID.Or -> FilterGroup.Or.Facet(value, key.name)
        }
    } + getTagGroups().map { (key, value) ->
        when (key) {
            is FilterGroupID.And -> FilterGroup.And.Tag(value, key.name)
            is FilterGroupID.Or -> FilterGroup.Or.Tag(value, key.name)
        }
    } + getNumericGroups().map { (key, value) ->
        when (key) {
            is FilterGroupID.And -> FilterGroup.And.Numeric(value, key.name)
            is FilterGroupID.Or -> FilterGroup.Or.Numeric(value, key.name)
        }
    }
}

public inline fun <reified T : Filter> MutableFilters.add(groupID: FilterGroupID, filters: Set<T>) {
    add(groupID, *filters.toTypedArray())
}

public inline fun <reified T : Filter> MutableFilters.remove(groupID: FilterGroupID, filters: Set<T>) {
    remove(groupID, *filters.toTypedArray())
}

internal fun Facet.toFilter(attribute: Attribute): Filter.Facet {
    return Filter.Facet(attribute, value)
}