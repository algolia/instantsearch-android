package com.algolia.instantsearch.helper.hierarchical

import com.algolia.search.model.search.Facet


internal fun HierarchicalTree.findNode(facet: Facet) : HierarchicalNode? = children.findNode(facet)

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

internal fun List<HierarchicalNode>.asTree(): HierarchicalTree {
    val tree = HierarchicalTree()

    forEach { node ->
        val root = tree.findNode(node.facet)

        if (root != null) {
            root.children += node
        } else {
            tree.children += node
        }
    }
    return tree
}

internal fun <T> HierarchicalTree.depth(
    comparator: Comparator<T>,
    level: Int = 0,
    transform: (HierarchicalNode, Int, Boolean) -> T
): List<T> {
    return children.depth(comparator, level, transform)
}

private fun <T> List<HierarchicalNode>.depth(
    comparator: Comparator<T>,
    level: Int = 0,
    transform: (HierarchicalNode, Int, Boolean) -> T
): List<T> {
    val list = mutableListOf<T>()

    forEach { node ->
        list += transform(node, level, node.children.isEmpty())
        list += node.children.depth(comparator, level + 1, transform)
    }
    return list.sortedWith(comparator)
}