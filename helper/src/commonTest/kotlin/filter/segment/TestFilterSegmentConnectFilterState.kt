package filter.segment

import com.algolia.instantsearch.helper.filter.segment.FilterSegmentViewModel
import com.algolia.instantsearch.helper.filter.segment.connectFilterState
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import shouldBeEmpty
import shouldEqual
import kotlin.test.Test


class TestFilterSegmentConnectFilterState {

    private val color = Attribute("color")
    private val groupID = FilterGroupID.Or(color)
    private val red = Filter.Facet(color, "red")
    private val id = 0
    private val filters = mapOf(id to red)
    private val expectedFilterState = FilterState(facetGroups = mutableMapOf(groupID to setOf(red)))

    @Test
    fun connectShouldUpdateSelectedWithFilterState() {
        val viewModel = FilterSegmentViewModel(filters)

        viewModel.connectFilterState(expectedFilterState, groupID)
        viewModel.selected shouldEqual id
    }

    @Test
    fun onSelectionsComputedShouldUpdateFilterState() {
        val viewModel = FilterSegmentViewModel(filters)
        val filterState = FilterState()

        viewModel.connectFilterState(filterState, groupID)
        viewModel.computeSelected(id)
        filterState.filters shouldEqual expectedFilterState.filters
    }

    @Test
    fun selectingTwiceShouldRemoveFilter() {
        val viewModel = FilterSegmentViewModel(filters)
        val filterState = FilterState()

        viewModel.connectFilterState(filterState, groupID)
        viewModel.computeSelected(id)
        viewModel.computeSelected(id)
        filterState.getFilters(groupID).shouldBeEmpty()
    }

    @Test
    fun onFilterStateChangedShouldUpdateSelections() {
        val viewModel = FilterSegmentViewModel(filters)
        val filterState = FilterState()

        viewModel.connectFilterState(filterState, groupID)
        filterState.notify { add(groupID, red) }
        viewModel.selected shouldEqual id
    }
}