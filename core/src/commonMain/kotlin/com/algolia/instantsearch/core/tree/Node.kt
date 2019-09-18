package com.algolia.instantsearch.core.tree

import kotlin.jvm.JvmOverloads


/**
 * A Node containing some [content] that might contain [children] nodes.
 *
 * @param content the node's value.
 * @param children optional nodes below this one.
 */
public data class Node<T> @JvmOverloads constructor(
    val content: T,
    val children: MutableList<Node<T>> = mutableListOf()
)
