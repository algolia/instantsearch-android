package com.algolia.instantsearch.core.tree

import kotlin.jvm.JvmField

/**
 * A Tree containing [nodes][Node] that might contain children.
 */
public data class Tree<T>(
    /**
     * The nodes contained in this tree.
     */
    @JvmField val children: MutableList<Node<T>> = mutableListOf()
)
