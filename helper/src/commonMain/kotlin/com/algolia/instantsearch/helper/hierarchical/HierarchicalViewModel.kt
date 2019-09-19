package com.algolia.instantsearch.helper.hierarchical

import com.algolia.instantsearch.core.subscription.SubscriptionEvent
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.core.tree.TreeViewModel
import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Facet
import kotlin.jvm.JvmOverloads

/**
 * A ViewModel storing hierarchical facets, represented as a tree of attributes with facets as leaves.
 *
 * @param attribute the faceted Attribute.
 * @param hierarchicalAttributes attributes for each level of the hierarchy.
 * **These MUST be specified in order**, e.g. `["hierarchy.lvl0", "hierarchy.lvl1", "hierarchy.lvl2"]`
 * @param separator the separator used in your category attribute.
 * @param tree an initial HierarchicalTree.
 */
public open class HierarchicalViewModel @JvmOverloads constructor(
    public val attribute: Attribute,
    public val hierarchicalAttributes: List<Attribute>,
    public val separator: String,
    tree: HierarchicalTree = HierarchicalTree()
) : TreeViewModel<String, Facet>(tree) {

    /**
     * The currently selected facets.
     */
    public val selections = SubscriptionValue<List<String>>(listOf())
    /**
     * Event fired whenever the hierarchical path changes.
     */
    public val eventHierarchicalPath = SubscriptionEvent<HierarchicalPath>()

    init {
        require(hierarchicalAttributes.isNotEmpty()) { "HierarchicalAttributes should not be empty" }
    }

    /**
     * Computes selected levels as a List of Pair([Attribute], value) given a hierarchical key.
     *
     * @param key a hierarchy level separated by [separator], e.g. "products > shoes > running"
     *
     */
    override fun computeSelections(key: String) {
        val selections = key.toSelectionList()
        val hierarchicalPath = hierarchicalAttributes.mapIndexed { index, item ->
            selections.getOrNull(index)?.let { item to it }
        }.filterNotNull()

        eventHierarchicalPath.send(hierarchicalPath)
    }

    private fun String.toSelectionList(): List<String> = split(separator).fold(listOf()) { acc, s ->
        acc + if (acc.isEmpty()) s else acc.last() + separator + s
    }
}