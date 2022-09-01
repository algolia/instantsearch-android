package com.algolia.instantsearch.compose.hierarchical.internal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.algolia.instantsearch.compose.hierarchical.HierarchicalState
import com.algolia.instantsearch.compose.internal.trace
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.hierarchical.HierarchicalItem

/**
 * Implementation of [HierarchicalState].
 *
 * @param hierarchicalItems initial hierarchical items list
 */
internal class HierarchicalStateImpl(
    hierarchicalItems: List<HierarchicalItem>
) : HierarchicalState {

    override var hierarchicalItems: List<HierarchicalItem> by mutableStateOf(hierarchicalItems)
    override var onSelectionChanged: Callback<String>? = null

    init {
        trace()
    }

    override fun setTree(tree: List<HierarchicalItem>) {
        this.hierarchicalItems = tree
    }
}
