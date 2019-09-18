package com.algolia.instantsearch.core.tree

/**
 * Finds the last (depth-first) node in this [Tree] that is validated by [isMatchingNode].
 *
 * @param content the expected node's content.
 * @param isMatchingNode a function that tells if the given [Node] matches the [content] to find.
 *
 * @return the last matching node, or `null` if none is found.
 */
public fun <T> Tree<T>.findNode(
    content: T,
    isMatchingNode: (T, Node<T>) -> Boolean
): Node<T>? = children.findNode(content, isMatchingNode)

/**
 * Finds the last (depth-first) node in this list that is validated by [isMatchingNode].
 *
 * @param content the expected node's content.
 * @param isMatchingNode a function that tells if the given [Node] matches the [content] to find.
 *
 * @return the last matching node, or `null` if none is found.
 */
public fun <T> List<Node<T>>.findNode(
    content: T,
    isMatchingNode: (T, Node<T>) -> Boolean
): Node<T>? {
    forEach { node ->
        if (isMatchingNode(content, node)) return node.children.findNode(content, isMatchingNode)
            ?: node
    }
    return null
}

/**
 * Transforms this list into a [Tree] of [Node`<T>`][Node].
 *
 * @param isMatchingNode a function that tells if a node matches a content,
 * in which case the new Node for this content will be added as a children of the given Node.
 */
public fun <T> List<T>.toNodes(
    isMatchingNode: (T, Node<T>) -> Boolean
): Tree<T> {
    return map { Node(it) }.asTree(isMatchingNode)
}

/**
 * Transforms this list into a [Tree] of [Node`<T>`][Node].
 *
 * @param isMatchingNode a function that tells if a node matches a content,
 * in which case a Node with this content will be added as a children of the other Node.
 */
public fun <T> List<Node<T>>.asTree(
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