package com.algolia.instantsearch.core.tree


public data class Node<T>(
    val content: T,
    val children: MutableList<Node<T>> = mutableListOf()
)
