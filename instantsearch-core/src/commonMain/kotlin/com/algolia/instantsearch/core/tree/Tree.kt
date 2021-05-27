package com.algolia.instantsearch.core.tree

public data class Tree<T>(
    val children: MutableList<Node<T>> = mutableListOf()
)
