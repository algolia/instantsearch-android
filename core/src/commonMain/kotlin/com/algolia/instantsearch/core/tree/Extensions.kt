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

fun <I, O> Tree<I>.asTree(
    comparator: Comparator<O>,
    transform: (Node<I>, Int, Boolean) -> O
): List<O> = children.asTree(comparator, 0, transform)


fun <I, O> List<Node<I>>.asTree(
    comparator: Comparator<O>,
    level: Int = 0,
    transform: (Node<I>, Int, Boolean) -> O
): List<O> {
    return mutableListOf<O>().also { list ->
        forEach { node ->
            list += transform(node, level, node.children.isEmpty())
            list += node.children.asTree(comparator, level + 1, transform)
        }
    }.sortedWith(comparator)
}