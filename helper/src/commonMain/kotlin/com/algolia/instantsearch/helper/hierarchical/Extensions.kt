package com.algolia.instantsearch.helper.hierarchical

import com.algolia.instantsearch.core.tree.Node
import com.algolia.instantsearch.core.tree.asTree
import com.algolia.instantsearch.core.tree.findNode
import com.algolia.instantsearch.core.tree.toNodes
import com.algolia.search.model.search.Facet


//FIXME: What if two values are prefixes but not subs? E.g "products > bike", "products > bikesheds"
val isMatchingFacetNode: (Facet, Node<Facet>) -> Boolean =
    { content, node -> content.value.startsWith(node.content.value) }

internal fun HierarchicalTree.findNode(facet: Facet) = findNode(facet, isMatchingFacetNode)

internal fun List<HierarchicalNode>.findNode(facet: Facet): HierarchicalNode? = findNode(facet, isMatchingFacetNode)

internal fun List<Facet>.toNodes(): HierarchicalTree = toNodes(isMatchingFacetNode)

internal fun List<HierarchicalNode>.asTree(): HierarchicalTree = asTree(isMatchingFacetNode)