package com.algolia.instantsearch.core.tree

public data class Node<T>(
    val content: T,
    val children: MutableList<Node<T>> = mutableListOf()
) {

    public var isSelected: Boolean = false

    constructor(
        content: T,
        isSelected: Boolean,
        children: MutableList<Node<T>> = mutableListOf()
    ) : this(content, children) {
        this.isSelected = isSelected
    }
}
