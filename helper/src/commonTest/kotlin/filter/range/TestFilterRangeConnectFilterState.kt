package filter.range

import com.algolia.instantsearch.core.number.Range
import com.algolia.instantsearch.core.number.range.NumberRangeViewModel
import com.algolia.instantsearch.helper.filter.range.connectFilterState
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import shouldBeNull
import shouldEqual
import kotlin.test.Test


class TestFilterRangeConnectFilterState  {

    private val attribute = Attribute("foo")
    private val filterGroupID = FilterGroupID(attribute)

    @Test
    fun onRangeComputedShouldUpdateFilterState() {
        val viewModel = NumberRangeViewModel.Int()
        val filterState = FilterState()
        val range = 0..9

        viewModel.connectFilterState(attribute, filterState)
        viewModel.range = Range.Int(range)
        filterState.getFilters() shouldEqual setOf(Filter.Numeric(attribute, range))
    }


    @Test
    fun onFilterStateChangedShouldUpdateRange() {
        val viewModel = NumberRangeViewModel.Int()
        val filterState = FilterState()
        val range = 0..9

        viewModel.connectFilterState(attribute, filterState)
        viewModel.range.shouldBeNull()
        filterState.notify {
            add(filterGroupID, Filter.Numeric(attribute, range))
        }
        viewModel.range shouldEqual Range.Int(range)
    }

    @Test
    fun onNegatedFilterStateChangedShouldUpdateRange() {
        val viewModel = NumberRangeViewModel.Int()
        val filterState = FilterState()
        val range = 0..9

        viewModel.connectFilterState(attribute, filterState)
        viewModel.range.shouldBeNull()
        filterState.notify {
            add(filterGroupID, Filter.Numeric(attribute, range, true))
        }
        viewModel.range shouldEqual Range.Int(range)
    }
}