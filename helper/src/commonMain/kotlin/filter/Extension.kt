package filter

import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.FilterGroup
import com.algolia.search.model.search.Facet
import search.GroupID


public fun FilterState.toFilterGroups(): List<FilterGroup<*>> {
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

internal fun <T : Filter> Map<GroupID, Set<T>>.getOrDefault(groupID: GroupID): Set<T> {
    return getOrElse(groupID, { setOf() })
}

internal fun <T : Filter> MutableMap<GroupID, Set<T>>.modify(
    groupID: GroupID,
    filter: T,
    operator: Set<T>.(T) -> Set<T>
) {
    val set = getOrDefault(groupID).operator(filter)

    if (set.isNotEmpty()) this[groupID] = set else remove(groupID)
}

internal fun <T : Filter> MutableMap<GroupID, Set<T>>.add(groupID: GroupID, filter: T) {
    modify(groupID, filter, { plus(it) })
}

internal fun <T : Filter> MutableMap<GroupID, Set<T>>.remove(groupID: GroupID, filter: T) {
    modify(groupID, filter, { minus(it) })
}

internal fun <T : Filter> MutableMap<GroupID, Set<T>>.clear(groupID: GroupID) {
    remove(groupID)
}

internal fun Facet.toFilter(attribute: Attribute): Filter.Facet {
    return Filter.Facet(attribute, value)
}