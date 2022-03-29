package com.algolia.instantsearch.hierarchical.internal

import com.algolia.instantsearch.core.tree.Node
import com.algolia.instantsearch.core.tree.asTree
import com.algolia.instantsearch.core.tree.findNode
import com.algolia.instantsearch.core.tree.toNodes
import com.algolia.instantsearch.hierarchical.HierarchicalNode
import com.algolia.instantsearch.hierarchical.HierarchicalTree
import com.algolia.search.model.search.Facet
import kotlin.math.min

private val isMatchingFacetNode: (Facet, Node<Facet>) -> Boolean =
    { content, node ->
        val regexSeparator = Regex(" . ")
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

internal fun HierarchicalTree.findNode(facet: Facet) = findNode(facet, isMatchingFacetNode)

internal fun List<HierarchicalNode>.findNode(facet: Facet): HierarchicalNode? = findNode(facet, isMatchingFacetNode)

internal fun List<Facet>.toNodes(hierarchicalValue: String? = null): HierarchicalTree {
    return toNodes(isMatchingFacetNode) { hierarchicalValue != null && it.value == hierarchicalValue }
}

internal fun List<HierarchicalNode>.asTree(): HierarchicalTree = asTree(isMatchingFacetNode)
