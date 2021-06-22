package com.algolia.instantsearch.compose.sortby

import com.algolia.instantsearch.compose.sortby.internal.SortByStateImpl
import com.algolia.instantsearch.helper.sortby.SortByView

/**
 * [SortByView] for compose.
 */
public interface SortByState : SortByView {

    /**
     * Sort by options.
     */
    public val options: Map<Int, String>

    /**
     * Index of selected option or `null` if none is selected.
     */
    public val selectedOption: Int?

    /**
     * Callback on sort by option selected.
     */
    public fun optionSelected(index: Int?)
}

/**
 * Creates an instance of [SortByState].
 *
 * @param options sort by option values
 * @param selectedOption index of selected option
 */
public fun SortByState(options: Map<Int, String> = emptyMap(), selectedOption: Int? = null): SortByState {
    return SortByStateImpl(options, selectedOption)
}
