package com.algolia.instantsearch.core.clickable


public class ClickableViewModel<T> {

    public val onClicked: MutableList<(T) -> Unit> = mutableListOf()

    public fun click(clicked: T) {
        onClicked.forEach { it(clicked) }
    }
}