package filter

import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.FilterGroup
import com.algolia.search.model.search.Facet
import search.GroupID


public fun Filters.toFilterGroups(): List<FilterGroup<*>> {
    return getFacets().map { (key, value) ->
        when (key) {
            is GroupID.And -> FilterGroup.And.Facet(value, key.name)
            is GroupID.Or -> FilterGroup.Or.Facet(value, key.name)
        }
    } + getTags().map { (key, value) ->
        when (key) {
            is GroupID.And -> FilterGroup.And.Tag(value, key.name)
            is GroupID.Or -> FilterGroup.Or.Tag(value, key.name)
        }
    } + getNumerics().map { (key, value) ->
        when (key) {
            is GroupID.And -> FilterGroup.And.Numeric(value, key.name)
            is GroupID.Or -> FilterGroup.Or.Numeric(value, key.name)
        }
    }
}

public inline fun <reified T : Filter> MutableFilters.add(groupID: GroupID, filters: Set<T>) {
    add(groupID, *filters.toTypedArray())
}

public inline fun <reified T : Filter> MutableFilters.remove(groupID: GroupID, filters: Set<T>) {
    remove(groupID, *filters.toTypedArray())
}

internal fun Facet.toFilter(attribute: Attribute): Filter.Facet {
    return Filter.Facet(attribute, value)
}