package filter

import com.algolia.search.model.filter.Filter
import search.GroupID


public data class FilterState(
    private val facets: MutableMap<GroupID, Set<Filter.Facet>> = mutableMapOf(),
    private val tags: MutableMap<GroupID, Set<Filter.Tag>> = mutableMapOf(),
    private val numerics: MutableMap<GroupID, Set<Filter.Numeric>> = mutableMapOf()
) {

    public val listeners: MutableList<(FilterState) -> Unit> = mutableListOf()

    public fun transaction(block: FilterState.() -> Unit) {
        block(this)
        listeners.forEach { it(this) }
    }

    fun triggerListeners() {
        listeners.forEach { it(this) }
    }

    public inline fun <reified T : Filter> add(groupID: GroupID, filters: Set<T>) {
        add(groupID, *filters.toTypedArray())
    }

    public inline fun <reified T : Filter> remove(groupID: GroupID, filters: Set<T>) {
        remove(groupID, *filters.toTypedArray())
    }

    public fun <T : Filter> add(groupID: GroupID, vararg filters: T) {
        filters.forEach { filter ->
            when (filter) {
                is Filter.Facet -> facets.add(groupID, filter)
                is Filter.Tag -> tags.add(groupID, filter)
                is Filter.Numeric -> numerics.add(groupID, filter)
            }
        }
    }

    public fun <T : Filter> remove(groupID: GroupID, vararg filters: T) {
        filters.forEach { filter ->
            when (filter) {
                is Filter.Facet -> facets.remove(groupID, filter)
                is Filter.Tag -> tags.remove(groupID, filter)
                is Filter.Numeric -> numerics.remove(groupID, filter)
            }
        }
    }

    public fun <T : Filter> toggle(groupID: GroupID, filter: T) {
        if (contains(groupID, filter)) remove(groupID, filter) else add(groupID, filter)
    }


    public fun <T : Filter> contains(groupID: GroupID, filter: T): Boolean {
        return when (filter) {
            is Filter.Facet -> facets[groupID]?.contains(filter)
            is Filter.Tag -> tags[groupID]?.contains(filter)
            is Filter.Numeric -> numerics[groupID]?.contains(filter)
            else -> null
        } ?: false
    }

    public fun clear(groupID: GroupID? = null) {
        if (groupID != null) {
            facets.clear(groupID)
            numerics.clear(groupID)
            tags.clear(groupID)
        } else {
            facets.clear()
            numerics.clear()
            tags.clear()
        }
    }

    public fun getFacets(groupID: GroupID): Set<Filter.Facet>? {
        return facets[groupID]
    }

    public fun getTags(groupID: GroupID): Set<Filter.Tag>? {
        return tags[groupID]
    }

    public fun getNumerics(groupID: GroupID): Set<Filter.Numeric>? {
        return numerics[groupID]
    }

    public fun getFacets(): Map<GroupID, Set<Filter.Facet>> {
        return facets
    }

    public fun getTags(): Map<GroupID, Set<Filter.Tag>> {
        return tags
    }

    public fun getNumerics(): Map<GroupID, Set<Filter.Numeric>> {
        return numerics
    }
}