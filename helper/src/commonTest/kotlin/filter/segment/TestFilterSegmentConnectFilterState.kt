package filter.segment

import com.algolia.instantsearch.helper.filter.segment.FilterSegmentViewModel
import com.algolia.instantsearch.helper.filter.segment.connectionFilterState
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import shouldBeEmpty
import shouldEqual
import kotlin.test.Test


class TestFilterSegmentConnectFilterState {

    private val color = Attribute("color")
    private val groupID = FilterGroupID(color, FilterOperator.Or)
    private val red = Filter.Facet(color, "red")
    private val id = 0
    private val filters = mapOf(id to red)
    private val expectedFilterState = FilterState(mapOf(groupID to setOf(red)))

    @Test
    fun connectShouldUpdateSelectedWithFilterState() {
        val viewModel = FilterSegmentViewModel(filters)
        val connection = viewModel.connectionFilterState(expectedFilterState, groupID)

        connection.connect()
        viewModel.selected.value shouldEqual id
    }

    @Test
    fun onSelectionsComputedShouldUpdateFilterState() {
        val viewModel = FilterSegmentViewModel(filters)
        val filterState = FilterState()
        val connection = viewModel.connectionFilterState(filterState, groupID)

        connection.connect()
        viewModel.eventSelection.send(id)
        filterState shouldEqual expectedFilterState
    }

    @Test
    fun selectingTwiceShouldRemoveFilter() {
        val viewModel = FilterSegmentViewModel(filters)
        val filterState = FilterState()
        val connection = viewModel.connectionFilterState(filterState, groupID)

        connection.connect()
        viewModel.eventSelection.send(id)
        viewModel.eventSelection.send(id)
        filterState.getFilters(groupID).shouldBeEmpty()
    }

    @Test
    fun onFilterStateChangedShouldUpdateSelections() {
        val viewModel = FilterSegmentViewModel(filters)
        val filterState = FilterState()
        val connection = viewModel.connectionFilterState(filterState, groupID)

        connection.connect()
        filterState.notify { add(groupID, red) }
        viewModel.selected.value shouldEqual id
    }
}