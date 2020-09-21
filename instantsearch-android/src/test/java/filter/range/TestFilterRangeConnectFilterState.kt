package filter.range

import com.algolia.instantsearch.core.number.range.NumberRangeViewModel
import com.algolia.instantsearch.core.number.range.Range
import com.algolia.instantsearch.helper.filter.range.connectFilterState
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import shouldBeNull
import shouldEqual
import kotlin.test.Test

class TestFilterRangeConnectFilterState {

    private val attribute = Attribute("foo")
    private val filterGroupID = FilterGroupID(attribute)

    @Test
    fun onRangeComputedShouldUpdateFilterState() {
        val viewModel = NumberRangeViewModel<Int>()
        val filterState = FilterState()
        val range = 0..9
        val connection = viewModel.connectFilterState(filterState, attribute, filterGroupID)

        connection.connect()
        viewModel.coerce(Range(range))
        filterState.getFilters() shouldEqual setOf(Filter.Numeric(attribute, range))
    }

    @Test
    fun onFilterStateChangedShouldUpdateRange() {
        val viewModel = NumberRangeViewModel<Float>()
        val filterState = FilterState()
        val range = 0f..9f
        val connection = viewModel.connectFilterState(filterState, attribute, filterGroupID)

        connection.connect()
        viewModel.range.value.shouldBeNull()
        filterState.notify {
            add(filterGroupID, Filter.Numeric(attribute, range.start, range.endInclusive))
        }
        viewModel.range.value shouldEqual Range(range)
    }

    @Test
    fun onNegatedFilterStateChangedShouldUpdateRange() {
        val viewModel = NumberRangeViewModel<Long>()
        val filterState = FilterState()
        val range = 0L..9L
        val connection = viewModel.connectFilterState(filterState, attribute, filterGroupID)

        connection.connect()
        viewModel.range.value.shouldBeNull()
        filterState.notify {
            add(filterGroupID, Filter.Numeric(attribute, range, true))
        }
        viewModel.range.value shouldEqual Range(range)
    }
}
