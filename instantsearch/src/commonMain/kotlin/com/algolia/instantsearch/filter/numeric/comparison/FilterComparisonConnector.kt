package com.algolia.instantsearch.filter.numeric.comparison

import com.algolia.instantsearch.core.connection.AbstractConnection
import com.algolia.instantsearch.core.number.NumberViewModel
import com.algolia.instantsearch.extension.traceNumberFilterConnector
import com.algolia.instantsearch.filter.state.FilterGroupID
import com.algolia.instantsearch.filter.state.FilterOperator
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.migration2to3.Attribute
import com.algolia.instantsearch.migration2to3.NumericOperator

/**
 * Filter Numeric Comparison is a view to filter on a numeric value using a comparison operator.
 *
 * @param viewModel the logic applied to the numeric value
 * @param filterState the FilterState that holds filters
 * @param attribute the attribute to filter on
 * @param operator the NumericOperator used to perform a numeric comparison
 * @param groupID groups all created filters under an ID and composes them with this operator
 */
public data class FilterComparisonConnector<T>(
    public val viewModel: NumberViewModel<T>,
    public val filterState: FilterState,
    public val attribute: Attribute,
    public val operator: NumericOperator,
    public val groupID: FilterGroupID = FilterGroupID(attribute, FilterOperator.And),
) : AbstractConnection() where T : Number, T : Comparable<T> {

    /**
     * @param filterState the FilterState that holds filters
     * @param attribute the attribute to filter on
     * @param operator the NumericOperator used to perform a numeric comparison
     * @param number initial numeric value to filter on
     * @param groupID groups all created filters under an ID and composes them with this operator
     */
    public constructor(
        filterState: FilterState,
        attribute: Attribute,
        operator: NumericOperator,
        number: T? = null,
        groupID: FilterGroupID = FilterGroupID(attribute, FilterOperator.And),
    ) : this(NumberViewModel(number), filterState, attribute, operator, groupID)

    private val connectionFilterState = viewModel.connectFilterState(filterState, attribute, operator, groupID)

    init {
        traceNumberFilterConnector()
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
