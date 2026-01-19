package com.algolia.instantsearch.hierarchical.internal

import com.algolia.instantsearch.core.tree.Node
import com.algolia.instantsearch.core.tree.asTree
import com.algolia.instantsearch.core.tree.findNode
import com.algolia.instantsearch.core.tree.toNodes
import com.algolia.instantsearch.hierarchical.HierarchicalNode
import com.algolia.instantsearch.hierarchical.HierarchicalTree
import com.algolia.instantsearch.filter.Facet
import kotlin.math.min

internal const val DefaultSeparator = " . "

private val isMatchingFacetNode: (Facet, Node<Facet>, String) -> Boolean =
    { content, node, pattern ->
        val regexSeparator = Regex(pattern)
        val splitContent: List<String> = content.value.split(regexSeparator)
        val splitNode: List<String> = node.content.value.split(regexSeparator)
        var isMatching = true

        for (i in 0 until min(splitContent.size, splitNode.size)) {
            if (splitContent[i] != splitNode[i]) {
                isMatching = false
                break
            }
        }
        isMatching
    }

internal fun HierarchicalTree.findNode(facet: Facet, separator: String = DefaultSeparator) =
    findNode(separator, facet, isMatchingFacetNode)

internal fun List<HierarchicalNode>.findNode(facet: Facet, separator: String = DefaultSeparator): HierarchicalNode? =
    findNode(separator, facet, isMatchingFacetNode)

internal fun List<Facet>.toNodes(
    hierarchicalValue: String? = null,
    separator: String = DefaultSeparator
): HierarchicalTree {
    return toNodes(separator, isMatchingFacetNode) { hierarchicalValue != null && it.value == hierarchicalValue }
}

internal fun List<HierarchicalNode>.asTree(separator: String = DefaultSeparator): HierarchicalTree =
    asTree(separator, isMatchingFacetNode)
