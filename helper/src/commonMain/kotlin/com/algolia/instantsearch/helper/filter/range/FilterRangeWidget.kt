package com.algolia.instantsearch.helper.filter.range

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.number.range.Range
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.Attribute


public class FilterRangeWidget<T>(
    public val viewModel: FilterRangeViewModel<T>,
    public val filterState: FilterState,
    public val attribute: Attribute,
    public val groupID: FilterGroupID = FilterGroupID(attribute, FilterOperator.And)
) : ConnectionImpl() where T : Number, T : Comparable<T> {

    public constructor(
        filterState: FilterState,
        attribute: Attribute,
        bounds: ClosedRange<T>? = null,
        range: ClosedRange<T>? = null
    ) : this(
        FilterRangeViewModel(
            range = range?.let { Range(it) },
            bounds = bounds?.let { Range(it) }
        ),
        filterState, attribute
    )

    private val connectionFilterState = viewModel.connectionFilterState(filterState, attribute, groupID)

    override fun connect() {
        super.connect()
        connectionFilterState.connect()
    }

    override fun disconnect() {
        super.disconnect()
        connectionFilterState.disconnect()
    }
}