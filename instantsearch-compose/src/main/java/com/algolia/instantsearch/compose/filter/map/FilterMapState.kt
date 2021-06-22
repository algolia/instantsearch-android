package com.algolia.instantsearch.compose.filter.map

import com.algolia.instantsearch.compose.filter.map.internal.FilterMapStateImpl
import com.algolia.instantsearch.helper.filter.map.FilterMapView

/**
 * [FilterMapView] for compose.
 */
public interface FilterMapState : FilterMapView {

    /**
     * List of radio options.
     */
    public val radioOptions: List<String>

    /**
     * Index of selected option, or `null` if none is selected.
     */
    public val selectedOption: Int?

    /**
     * Callback on option Select.
     */
    public fun optionSelected(selected: Int)
}

/**
 *  Creates an instance of [FilterMapState].
 *
 *  @param radioOptions radio options list
 *  @param selectedOption selected option index
 */
public fun FilterMapState(radioOptions: List<String> = emptyList(), selectedOption: Int? = null): FilterMapState {
    return FilterMapStateImpl(radioOptions, selectedOption)
}
