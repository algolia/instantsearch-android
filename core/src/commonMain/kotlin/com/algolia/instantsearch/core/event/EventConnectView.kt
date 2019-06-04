package com.algolia.instantsearch.core.event


public fun <I, O> EventViewModel<I>.connectView(view: EventView<O>, trigger: () -> Unit) {
    view.onClick = { trigger() }
}