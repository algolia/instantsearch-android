package com.algolia.instantsearch.filter.range

import com.algolia.instantsearch.core.connection.AbstractConnection
import com.algolia.instantsearch.core.number.range.Range
import com.algolia.instantsearch.extension.traceNumberRangeFilterConnector
import com.algolia.instantsearch.filter.state.FilterGroupID
import com.algolia.instantsearch.filter.state.FilterOperator
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.search.model.Attribute

/**
 * Filter Numeric Range is a filtering view made to filter between two numeric values.
 * The most common interface for this is a slider.
 * [Documentation](https://www.algolia.com/doc/api-reference/widgets/range-slider/android/)
 *
 * @param viewModel the logic applied to the numeric ranges.
 * @param filterState the FilterState that will hold your filters
 * @param attribute the attribute to filter
 * @param groupID the identifier of the group of filters
 */
public data class FilterRangeConnector<T>(
    public val viewModel: FilterRangeViewModel<T>,
    public val filterState: FilterState,
    public val attribute: Attribute,
    public val groupID: FilterGroupID = FilterGroupID(attribute, FilterOperator.And),
) : AbstractConnection() where T : Number, T : Comparable<T> {

    /**
     * @param filterState the FilterState that will hold your filters
     * @param attribute the attribute to filter
     * @param bounds the limits of the acceptable range within which values will be coerced
     * @param range the range of values withing the bounds
     */
    public constructor(
        filterState: FilterState,
        attribute: Attribute,
        bounds: ClosedRange<T>? = null,
        range: ClosedRange<T>? = null,
    ) : this(
        FilterRangeViewModel(
            range = range?.let { Range(it) },
            bounds = bounds?.let { Range(it) }
        ),
        filterState, attribute
    )

    private val connectionFilterState = viewModel.connectFilterState(filterState, attribute, groupID)

    init {
        traceNumberRangeFilterConnector()
    }

    override fun connect() {
        super.connect()
        connectionFilterState.connect()
    }

    override fun disconnect() {
        super.disconnect()
        connectionFilterState.disconnect()
    }
}
