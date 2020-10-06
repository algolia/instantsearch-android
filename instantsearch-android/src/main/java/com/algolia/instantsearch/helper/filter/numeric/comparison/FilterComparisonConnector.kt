package com.algolia.instantsearch.helper.filter.numeric.comparison

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.number.NumberViewModel
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.NumericOperator

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
) : ConnectionImpl() where T : Number, T : Comparable<T> {

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

    override fun connect() {
        super.connect()
        connectionFilterState.connect()
    }

    override fun disconnect() {
        super.disconnect()
        connectionFilterState.disconnect()
    }
}
