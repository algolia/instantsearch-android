package filter.numeric.comparison

import com.algolia.instantsearch.core.number.NumberViewModel
import com.algolia.instantsearch.filter.numeric.comparison.connectFilterState
import com.algolia.instantsearch.filter.state.FilterGroupID
import com.algolia.instantsearch.filter.state.FilterOperator
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.filter.Filter
import com.algolia.instantsearch.filter.NumericOperator
import shouldEqual
import kotlin.test.Test

class TestFilterComparisonConnectFilterState {

    private val price = "price"
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
