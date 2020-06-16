package com.algolia.instantsearch.insights.event

/**
 * An array of objects associated with this event. See [IDs] and [Filters].
 */
public sealed class EventObjects(val values: List<String>) {
    /** An array of index objectID. **Limited to 20 objects.** */
    data class IDs(val iDs: List<String>) : EventObjects(iDs) {
        constructor(vararg iDs: String) : this(iDs.toList())
    }

    /** An array of filters. **Limited to 10 filters.** */
    data class Filters(val filters: List<String>) : EventObjects(filters) {
        constructor(vararg filters: String) : this(filters.toList())
    }
}
