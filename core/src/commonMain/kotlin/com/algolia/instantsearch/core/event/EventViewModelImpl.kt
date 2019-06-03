package com.algolia.instantsearch.core.event


public open class EventViewModelImpl<T>: EventViewModel<T> {

    override val onTriggered: MutableList<(T) -> Unit> = mutableListOf()
}