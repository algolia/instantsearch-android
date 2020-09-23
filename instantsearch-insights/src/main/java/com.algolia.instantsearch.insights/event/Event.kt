package com.algolia.instantsearch.insights.event

public sealed class Event {

    data class Click(
        val eventName: String,
        val userToken: String,
        val timestamp: Long,
        val eventObjects: EventObjects,
        val queryId: String? = null,
        val positions: List<Int>? = null,
    ) : Event()

    data class View(
        val eventName: String,
        val userToken: String,
        val timestamp: Long,
        val eventObjects: EventObjects,
        val queryId: String? = null,
    ) : Event()

    data class Conversion(
        val eventName: String,
        val userToken: String,
        val timestamp: Long,
        val eventObjects: EventObjects,
        val queryId: String? = null,
    ) : Event()
}
