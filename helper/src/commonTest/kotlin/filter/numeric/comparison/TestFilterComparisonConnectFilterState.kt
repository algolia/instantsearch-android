package filter.numeric.comparison

import com.algolia.instantsearch.core.selectable.number.SelectableNumberViewModel
import com.algolia.instantsearch.helper.filter.numeric.comparison.connectFilterState
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator
import shouldEqual
import kotlin.test.Test


class TestFilterComparisonConnectFilterState  {

    private val price = Attribute("price")
    private val operator =  NumericOperator.Greater
    private val filter = Filter.Numeric(price, operator, 5)
    private val groupID = FilterGroupID(price, FilterOperator.And)
    private val expectedFilterState = FilterState(mapOf(groupID to setOf(filter)))

    @Test
    fun connectShouldUpdateNumberWithFilterState() {
        val viewModel = SelectableNumberViewModel.Int(0 .. 10)
        val filterState = FilterState()

        viewModel.connectFilterState(price, operator, filterState, groupID)
        viewModel.item shouldEqual null
    }

    @Test
    fun onSelectionsComputedShouldUpdateFilterState() {
        val viewModel = SelectableNumberViewModel.Int(0 .. 10)
        val filterState = FilterState()

        viewModel.connectFilterState(price, operator, filterState, groupID)
        viewModel.computeNumber(5)
        filterState shouldEqual expectedFilterState
    }
}