package com.algolia.instantsearch.helper.hierarchical

import com.algolia.search.model.search.Facet


internal fun HierarchicalTree.findNode(facet: Facet): HierarchicalNode? = children.findNode(facet)

internal fun List<HierarchicalNode>.findNode(facet: Facet): HierarchicalNode? {
    forEach { node ->
        if (facet.value.startsWith(node.content.value))
            return node.children.findNode(facet) ?: node
    }
    return null
}

internal fun List<Facet>.toNodes(): HierarchicalTree {
    return map { HierarchicalNode(it) }.asTree()
}

internal fun List<HierarchicalNode>.asTree(): HierarchicalTree = HierarchicalTree().also { tree ->
    forEach { node ->
        val root = tree.findNode(node.content)

        if (root != null) {
            root.children += node
        } else {
            tree.children += node
        }
    }
}

internal fun <T> HierarchicalTree.asTree(
    comparator: Comparator<T>,
    transform: (HierarchicalNode, Int, Boolean) -> T
): List<T> = children.asTree(comparator, 0, transform)

private fun <T> List<HierarchicalNode>.asTree(
    comparator: Comparator<T>,
    level: Int = 0,
    transform: (HierarchicalNode, Int, Boolean) -> T
): List<T> {
    return mutableListOf<T>().also { list ->
        forEach { node ->
            list += transform(node, level, node.children.isEmpty())
            list += node.children.asTree(comparator, level + 1, transform)
        }
    }.sortedWith(comparator)
}