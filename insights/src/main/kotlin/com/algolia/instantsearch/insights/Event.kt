package com.algolia.instantsearch.insights


internal sealed class Event(open val params: Map<String, Any>) {

    data class View(override val params: Map<String, Any>) : Event(params)

    data class Click(override val params: Map<String, Any>) : Event(params)

    data class Conversion(override val params: Map<String, Any>) : Event(params)
}
