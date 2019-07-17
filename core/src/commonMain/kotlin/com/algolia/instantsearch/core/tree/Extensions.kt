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

fun <T> List<T>.toNodes(
    isMatchingNode: (T, Node<T>) -> Boolean
): Tree<T> {
    return map { Node(it) }.asTree(isMatchingNode)
}

fun <T> List<Node<T>>.asTree(
    isMatchingNode: (T, Node<T>) -> Boolean
): Tree<T> = Tree<T>().also { tree ->
    forEach { node ->
        val root = tree.findNode(node.content, isMatchingNode)

        if (root != null) {
            root.children += node
        } else {
            tree.children += node
        }
    }
}

/**
 * Transforms a List of Nodes with children into a flat list matching the nodes hierarchy.
 *
 * @param comparator a [Comparator] to sort sibling nodes.
 * @param transform A function transforming a Node`<`**I**`>` into an **O**, knowing its depth and if it has children.
 */
public fun <I, O> Tree<I>.asTree(
    comparator: Comparator<O>,
    transform: (Node<I>, Int, Boolean) -> O
): List<O> = children.asTree(0, transform).sortedWith(comparator)


private fun <I, O> List<Node<I>>.asTree(
    level: Int = 0,
    transform: (Node<I>, Int, Boolean) -> O
): List<O> {
    return mutableListOf<O>().also { list ->
        forEach { node ->
            list += transform(node, level, node.children.isEmpty())
            list += node.children.asTree(level + 1, transform)
        }
    }
}