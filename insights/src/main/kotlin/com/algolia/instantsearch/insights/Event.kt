package com.algolia.instantsearch.insights


internal sealed class Event(open val params: EventParameters) {

    data class View(override val params: EventParameters) : Event(params)

    data class Click(override val params: EventParameters) : Event(params)

    data class Conversion(override val params: EventParameters) : Event(params)
}
