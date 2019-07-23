package com.algolia.instantsearch.helper.filter.range

import com.algolia.instantsearch.core.connection.Connections
import com.algolia.instantsearch.core.number.range.NumberRangeView
import com.algolia.instantsearch.core.number.range.Range
import com.algolia.instantsearch.core.number.range.connectionView
import com.algolia.instantsearch.helper.connection.ConnectionImplWidget
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.Attribute


public class FilterRangeWidget<T>(
    public val viewModel: FilterRangeViewModel<T>,
    public val filterState: FilterState,
    public val attribute: Attribute,
    public val groupID: FilterGroupID = FilterGroupID(attribute, FilterOperator.And)
) : ConnectionImplWidget() where T : Number, T : Comparable<T> {

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

    override val connections = listOf(viewModel.connectionFilterState(filterState, attribute, groupID))

    public fun with(vararg views: NumberRangeView<T>): Connections {
        return views.map(viewModel::connectionView)
    }
}