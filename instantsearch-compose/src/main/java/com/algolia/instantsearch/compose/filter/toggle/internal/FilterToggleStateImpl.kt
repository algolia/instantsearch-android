package com.algolia.instantsearch.compose.filter.toggle.internal

import com.algolia.instantsearch.compose.filter.toggle.FilterToggleState
import com.algolia.instantsearch.compose.internal.trace
import com.algolia.instantsearch.compose.selectable.SelectableItemState

internal class FilterToggleStateImpl(
    text: String,
    isSelected: Boolean
) : FilterToggleState, SelectableItemState<String> by SelectableItemState(text, isSelected) {

    init {
        trace()
    }

    override fun changeSelection(isSelected: Boolean) {
        onSelectionChanged?.invoke(isSelected)
    }
}
