package search

import com.algolia.search.model.filter.Filter
import kotlin.properties.Delegates


class SearchFilterState {

    private var state: FilterState by Delegates.observable(mapOf()) { _, oldValue, newValue ->
        if (newValue != oldValue) {
            stateListeners.forEach {
                it(newValue)
            }
        }
    }

    val stateListeners: MutableList<(FilterState) -> Unit> = mutableListOf()

    fun <T : Filter> replace(group: Group<T>, filters: Set<T>) {
        state = state.toMutableMap().apply {
            put(group, filters)
        }
    }

    fun <T : Filter> add(group: Group<T>, filter: T) {
        state = state.toMutableMap().apply {
            val filters = get(group).orEmpty().plus(filter)

            put(group, filters)
        }
    }

    fun <T : Filter> remove(group: Group<T>, filter: T): Boolean {
        val currentGroup = state[group]
        val contains = currentGroup?.contains(filter)

        state = state.toMutableMap().apply {
            if (contains == true) {
                put(group, currentGroup.minus(filter))
            }
        }
        return contains == true
    }

    fun clear(group: Group<*>? = null) {
        state = if (group != null) {
            state.toMutableMap().apply {
                remove(group)
            }
        } else mapOf()
    }

    fun get(): FilterState {
        return state
    }
}
