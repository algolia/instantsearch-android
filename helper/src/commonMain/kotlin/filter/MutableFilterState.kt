package filter

import com.algolia.search.model.filter.Filter
import search.GroupID
import kotlin.properties.Delegates


class MutableFilterState {

    private var state by Delegates.observable(FilterState()) { _, oldValue, newValue ->
        if (newValue != oldValue) {
            listeners.forEach {
                it(newValue)
            }
        }
    }

    val listeners: MutableList<(FilterState) -> Unit> = mutableListOf()

    fun <T : Filter> add(groupID: GroupID, vararg filters: T) {
        val facet = state.facet.toMutableMap()
        val tag = state.tag.toMutableMap()
        val numeric = state.numeric.toMutableMap()

        filters.forEach { filter ->
            when (filter) {
                is Filter.Facet -> facet.add(groupID, filter)
                is Filter.Tag -> tag.add(groupID, filter)
                is Filter.Numeric -> numeric.add(groupID, filter)
            }
        }

        state = state.copy(facet = facet, tag = tag, numeric = numeric)
    }

    inline fun <reified T : Filter> add(groupID: GroupID, filters: Set<T>) {
        return add(groupID, *filters.toTypedArray())
    }

    fun <T : Filter> remove(groupID: GroupID, vararg filters: T) {
        val facet = state.facet.toMutableMap()
        val tag = state.tag.toMutableMap()
        val numeric = state.numeric.toMutableMap()

        filters.forEach { filter ->
            when (filter) {
                is Filter.Facet -> facet.remove(groupID, filter)
                is Filter.Tag -> tag.remove(groupID, filter)
                is Filter.Numeric -> numeric.remove(groupID, filter)
            }
        }

        state = state.copy(facet = facet, tag = tag, numeric = numeric)
    }

    fun <T : Filter> contains(groupID: GroupID, filter: T): Boolean {
        return when (filter) {
            is Filter.Facet -> state.facet[groupID]?.contains(filter)
            is Filter.Tag -> state.tag[groupID]?.contains(filter)
            is Filter.Numeric -> state.numeric[groupID]?.contains(filter)
            else -> null
        } ?: false
    }

    fun <T : Filter> toggle(groupID: GroupID, filter: T) {
        return if (contains(groupID, filter)) remove(groupID, filter) else add(groupID, filter)
    }

    fun clear(groupID: GroupID? = null) {
        state = if (groupID != null) {
            state.copy(
                facet = state.facet.clear(groupID),
                numeric = state.numeric.clear(groupID),
                tag = state.tag.clear(groupID)
            )
        } else {
            state.copy(facet = mapOf(), numeric = mapOf(), tag = mapOf())
        }
    }

    fun get(): FilterState {
        return state
    }
}
