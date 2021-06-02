@file:Suppress("FunctionName")

package com.algolia.instantsearch.compose.filter.toggle

import com.algolia.instantsearch.compose.filter.toggle.internal.FilterToggleStateImpl
import com.algolia.instantsearch.helper.filter.toggle.FilterToggleView

/**
 * [FilterToggleView] for compose.
 */
public interface FilterToggleState : FilterToggleView {

    /**
     * Text to be displayed.
     */
    public val text: String

    /**
     * The button state, True if checked, otherwise false.
     */
    public val isSelected: Boolean

    /**
     * Change the selected state.
     *
     * @param isSelected the new checked state of buttonView.
     */
    public fun selectionChanged(isSelected: Boolean)
}

/**
 * Creates an instance of [FilterToggleState].
 *
 * @param text initial text value
 * @param isSelected initial selection value
 */
public fun FilterToggleState(text: String, isSelected: Boolean = false): FilterToggleState {
    return FilterToggleStateImpl(text, isSelected)
}
