package com.algolia.instantsearch.helper.hierarchical

import com.algolia.search.model.search.Facet


internal fun HierarchicalTree.findNode(facet: Facet): HierarchicalNode? = children.findNode(facet)

internal fun List<HierarchicalNode>.findNode(facet: Facet): HierarchicalNode? {
    forEach { node ->
        val next = node.children.findNode(facet)

        if (facet.value.startsWith(node.facet.value)) return next ?: node
    }
    return null
}

internal fun List<Facet>.toNodes(): HierarchicalTree {
    return map { HierarchicalNode(it) }.asTree()
}

internal fun List<HierarchicalNode>.asTree(): HierarchicalTree = HierarchicalTree().also { tree ->
    forEach { node ->
        val root = tree.findNode(node.facet)

        if (root != null) {
            root.children += node
        } else {
            tree.children += node
        }
    }
}

internal fun <T> HierarchicalTree.asTree(
    comparator: Comparator<T>,
    level: Int = 0,
    transform: (HierarchicalNode, Int, Boolean) -> T
): List<T> = children.asTree(comparator, level, transform)

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