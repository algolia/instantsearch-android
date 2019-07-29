package filter.numeric.comparison

import com.algolia.instantsearch.core.number.NumberViewModel
import com.algolia.instantsearch.helper.filter.numeric.comparison.connectFilterState
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator
import shouldEqual
import kotlin.test.Test


class TestFilterComparisonConnectFilterState {

    private val price = Attribute("price")
    private val operator = NumericOperator.Greater
    private val filter = Filter.Numeric(price, operator, 5)
    private val groupID = FilterGroupID(price, FilterOperator.And)
    private val expectedFilterState = FilterState(mapOf(groupID to setOf(filter)))

    @Test
    fun connectShouldUpdateNumberWithFilterState() {
        val viewModel = NumberViewModel(0..10)
        val filterState = FilterState()
        val connection = viewModel.connectFilterState(filterState, price, operator, groupID)

        connection.connect()
        viewModel.number.value shouldEqual null
    }

    @Test
    fun coerceShouldUpdateFilterState() {
        val viewModel = NumberViewModel(0..10)
        val filterState = FilterState()
        val connection = viewModel.connectFilterState(filterState, price, operator, groupID)

        connection.connect()
        viewModel.coerce(5)
        filterState shouldEqual expectedFilterState
    }
}