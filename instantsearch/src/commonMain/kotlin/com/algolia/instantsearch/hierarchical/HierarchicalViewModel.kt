package com.algolia.instantsearch.hierarchical

import com.algolia.instantsearch.core.subscription.SubscriptionEvent
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.core.tree.TreeViewModel
import com.algolia.instantsearch.extension.traceHierarchicalFacets

import com.algolia.instantsearch.filter.Facet

/**
 * @param hierarchicalAttributes attributes of the hierarchy.
 * These MUST be specified in order, e.g. `["hierarchy.lvl0", "hierarchy.lvl1", "hierarchy.lvl2"]`.
 */
public open class HierarchicalViewModel(
    public val attribute: String,
    public val hierarchicalAttributes: List<Attribute>,
    public val separator: String,
    tree: HierarchicalTree = HierarchicalTree(),
) : TreeViewModel<String, Facet>(tree) {

    public val selections: SubscriptionValue<List<String>> = SubscriptionValue(listOf())
    public val eventHierarchicalPath: SubscriptionEvent<HierarchicalPath> = SubscriptionEvent()

    internal val hierarchicalPath = SubscriptionValue<HierarchicalPath>(listOf())

    init {
        if (hierarchicalAttributes.isEmpty()) throw IllegalArgumentException("HierarchicalAttributes should not be empty")
        traceHierarchicalFacets()
    }

    /**
     * Computes selected levels as a List of Pair([Attribute], value) given a hierarchical key.
     *
     * @param key a hierarchy level separated by [separator], e.g. "products > shoes > running"
     *
     */
    override fun computeSelections(key: String) {
        val selections = key.toSelectionList()
        val updatedHierarchicalPath = hierarchicalAttributes
            .mapIndexed { index, item -> selections.getOrNull(index)?.let { item to it } }
            .filterNotNull()
            .updateIfDeselect(hierarchicalPath.value)

        hierarchicalPath.value = updatedHierarchicalPath
        eventHierarchicalPath.send(updatedHierarchicalPath)
    }

    private fun String.toSelectionList(): List<String> = split(separator).fold(listOf()) { acc, s ->
        acc + if (acc.isEmpty()) s else acc.last() + separator + s
    }

    /**
     * Remove the last item from the hierarchical path if it's a deselection operation.
     * Returning an empty list indicates that no item is selected.
     */
    private fun HierarchicalPath.updateIfDeselect(current: HierarchicalPath): HierarchicalPath {
        if (this != current) return this // new path selected, nothing to do
        return dropLast(1)
    }
}
