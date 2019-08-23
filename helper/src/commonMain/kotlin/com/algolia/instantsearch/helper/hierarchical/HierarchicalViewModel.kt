package com.algolia.instantsearch.helper.hierarchical

import com.algolia.instantsearch.core.subscription.SubscriptionEvent
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.core.tree.TreeViewModel
import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Facet

/**
 * @param hierarchicalAttributes attributes of the hierarchy.
 * These MUST be specified in order, e.g. `["hierarchy.lvl0", "hierarchy.lvl1", "hierarchy.lvl2"]`.
 */
public open class HierarchicalViewModel(
    public val attribute: Attribute,
    public val hierarchicalAttributes: List<Attribute>,
    public val separator: String,
    tree: HierarchicalTree = HierarchicalTree()
) : TreeViewModel<String, Facet>(tree) {

    public val selections = SubscriptionValue<List<String>>(listOf())
    public val eventHierarchicalPath = SubscriptionEvent<HierarchicalPath>()

    init {
        if (hierarchicalAttributes.isEmpty())
            throw IllegalArgumentException("HierarchicalAttributes should not be empty")
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