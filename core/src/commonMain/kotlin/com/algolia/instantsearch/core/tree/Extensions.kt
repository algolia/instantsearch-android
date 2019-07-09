package com.algolia.instantsearch.core.tree


fun <T> Tree<T>.findNode(
    content: T,
    isMatchingNode: (T, Node<T>) -> Boolean
): Node<T>? = children.findNode(content, isMatchingNode)

fun <T> List<Node<T>>.findNode(
    content: T,
    isMatchingNode: (T, Node<T>) -> Boolean
): Node<T>? {
    forEach { node ->
        if (isMatchingNode(content, node)) return node.children.findNode(content, isMatchingNode) ?: node
    }
    return null
}