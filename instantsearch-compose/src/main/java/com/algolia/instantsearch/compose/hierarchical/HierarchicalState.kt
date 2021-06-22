package com.algolia.instantsearch.compose.hierarchical

import com.algolia.instantsearch.compose.hierarchical.internal.HierarchicalStateImpl
import com.algolia.instantsearch.helper.hierarchical.HierarchicalItem
import com.algolia.instantsearch.helper.hierarchical.HierarchicalView

/**
 * [HierarchicalView] for compose.
 */
public interface HierarchicalState : HierarchicalView {

    /**
     * Hierarchical items list.
     */
    public val hierarchicalItems: List<HierarchicalItem>
}

/**
 * Creates an instance of HierarchicalState.
 *
 * @param hierarchicalItems initial hierarchical items list
 */
public fun HierarchicalState(hierarchicalItems: List<HierarchicalItem> = emptyList()): HierarchicalState {
    return HierarchicalStateImpl(hierarchicalItems)
}
