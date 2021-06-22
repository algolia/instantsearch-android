package com.algolia.instantsearch.compose.filter.map

import com.algolia.instantsearch.compose.filter.map.internal.FilterMapStateImpl
import com.algolia.instantsearch.helper.filter.map.FilterMapView

/**
 * [FilterMapView] for compose.
 */
public interface FilterMapState : FilterMapView {

    /**
     * Options values.
     */
    public val options: Map<Int, String>

    /**
     * Index of selected option, or `null` if none is selected.
     */
    public val selected: Int?

    /**
     * Callback on option Select.
     */
    public fun optionSelected(selected: Int)
}

/**
 *  Creates an instance of [FilterMapState].
 *
 *  @param options initial options list
 *  @param selected initial selected option index
 */
public fun FilterMapState(options: Map<Int, String> = emptyMap(), selected: Int? = null): FilterMapState {
    return FilterMapStateImpl(options, selected)
}
