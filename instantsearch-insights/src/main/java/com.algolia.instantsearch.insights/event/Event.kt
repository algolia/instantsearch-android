package com.algolia.instantsearch.insights.event

public sealed class Event {

    public data class Click(
        val eventName: String,
        val userToken: String,
        val timestamp: Long,
        val eventObjects: EventObjects,
        val queryId: String? = null,
        val positions: List<Int>? = null,
    ) : Event()

    public data class View(
        val eventName: String,
        val userToken: String,
        val timestamp: Long,
        val eventObjects: EventObjects,
        val queryId: String? = null,
    ) : Event()

    public data class Conversion(
        val eventName: String,
        val userToken: String,
        val timestamp: Long,
        val eventObjects: EventObjects,
        val queryId: String? = null,
    ) : Event()
}
