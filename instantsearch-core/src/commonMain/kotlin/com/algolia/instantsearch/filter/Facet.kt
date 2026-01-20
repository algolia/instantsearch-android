package com.algolia.instantsearch.filter

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Facet(
    /**
     * Name of the facet. Is equal to the value associated to an [Attribute].
     */
    @SerialName("value") val value: String,
    /**
     * Number of times this [value] occurs for a given [Attribute].
     */
    @SerialName("count") val count: Int,
    /**
     * Highlighted value.
     */
    @SerialName("highlighted") val highlightedOrNull: String? = null
) {

    val highlighted: String
        get() = highlightedOrNull!!
}

@OptIn(InternalSerializationApi::class)
public operator fun List<Facet>.get(value: String): Int {
    return find { it.value == value }!!.count
}

@OptIn(InternalSerializationApi::class)
public operator fun Map<Attribute, List<Facet>>.get(attribute: Attribute, value: String): Int {
    return getValue(attribute).find { it.value == value }!!.count
}
