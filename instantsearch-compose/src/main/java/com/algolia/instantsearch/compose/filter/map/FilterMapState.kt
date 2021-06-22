package com.algolia.instantsearch.compose.filter.map

import com.algolia.instantsearch.compose.filter.map.internal.FilterMapStateImpl
import com.algolia.instantsearch.helper.filter.map.FilterMapView

/**
 * [FilterMapView] for compose.
 */
public interface FilterMapState : FilterMapView {

    /**
     * Radio options values.
     */
    public val radioOptions: Map<Int, String>

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
public fun FilterMapState(radioOptions: Map<Int, String> = emptyMap(), selectedOption: Int? = null): FilterMapState {
    return FilterMapStateImpl(radioOptions, selectedOption)
}
