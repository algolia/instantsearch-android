package com.algolia.instantsearch.compose.filter.toggle

import com.algolia.instantsearch.compose.filter.toggle.internal.FilterToggleStateImpl
import com.algolia.instantsearch.compose.selectable.SelectableItemState
import com.algolia.instantsearch.filter.toggle.FilterToggleView

/**
 * [FilterToggleView] for compose.
 */
public interface FilterToggleState : FilterToggleView, SelectableItemState<String> {

    /**
     * Change the selected state.
     *
     * @param isSelected the new checked state of buttonView.
     */
    public fun changeSelection(isSelected: Boolean)
}

/**
 * Creates an instance of [FilterToggleState].
 *
 * @param text initial text value
 * @param isSelected initial selection value
 */
public fun FilterToggleState(text: String = "", isSelected: Boolean = false): FilterToggleState {
    return FilterToggleStateImpl(text, isSelected)
}
