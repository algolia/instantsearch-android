package com.algolia.instantsearch.core.tree

public data class Node<T>(
    val content: T,
    val children: MutableList<Node<T>> = mutableListOf()
) {

    public var selected: Boolean = false

    constructor(
        content: T,
        selected: Boolean,
        children: MutableList<Node<T>> = mutableListOf()
    ) : this(content, children) {
        this.selected = selected
    }
}
