package com.algolia.instantsearch.helper.filter.numeric.comparison

import com.algolia.instantsearch.core.connection.Connections
import com.algolia.instantsearch.core.connection.asList
import com.algolia.instantsearch.core.connection.connect
import com.algolia.instantsearch.core.number.*
import com.algolia.instantsearch.helper.connection.ConnectionImplWidget
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.NumericOperator


public class FilterComparisonWidget<T>(
    public val viewModel: NumberViewModel<T>,
    public val filterState: FilterState,
    public val attribute: Attribute,
    public val operator: NumericOperator,
    public val groupID: FilterGroupID = FilterGroupID(attribute, FilterOperator.And)
) : ConnectionImplWidget() where T : Number, T : Comparable<T> {

    constructor(
        filterState: FilterState,
        attribute: Attribute,
        operator: NumericOperator,
        number: T? = null,
        groupID: FilterGroupID = FilterGroupID(attribute, FilterOperator.And)
    ) : this(NumberViewModel(number), filterState, attribute, operator, groupID)

    override val connections = viewModel
        .connectionFilterState(filterState, attribute, operator, groupID)
        .asList()
        .connect()

    public fun with(
        vararg views: NumberView<T>,
        presenter: NumberPresenter<T> = NumberPresenterImpl
    ): Connections {
        return views.map { viewModel.connectionView(it, presenter) }.connect()
    }
}