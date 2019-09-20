package com.algolia.instantsearch.core.tree

import kotlin.jvm.JvmField
import kotlin.jvm.JvmOverloads


/**
 * A Node containing some [content] that might contain [children] nodes.
 *
 * @param content the node's value.
 * @param children optional nodes below this one.
 */
public data class Node<T> @JvmOverloads constructor(
    @JvmField
    val content: T,
    @JvmField
    val children: MutableList<Node<T>> = mutableListOf()
)
