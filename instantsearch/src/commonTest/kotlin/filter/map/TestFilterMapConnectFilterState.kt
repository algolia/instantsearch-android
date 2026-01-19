package filter.map

import com.algolia.instantsearch.filter.map.FilterMapViewModel
import com.algolia.instantsearch.filter.map.connectFilterState
import com.algolia.instantsearch.filter.state.FilterGroupID
import com.algolia.instantsearch.filter.state.FilterOperator
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import shouldBeEmpty
import shouldEqual
import kotlin.test.Test

class TestFilterMapConnectFilterState {

    private val color = String("color")
    private val groupID = FilterGroupID(color, FilterOperator.Or)
    private val red = Filter.Facet(color, "red")
    private val id = 0
    private val filters = mapOf(id to red)
    private val expectedFilterState = FilterState(mapOf(groupID to setOf(red)))

    @Test
    fun connectShouldUpdateSelectedWithFilterState() {
        val viewModel = FilterMapViewModel(filters)
        val connection = viewModel.connectFilterState(expectedFilterState, groupID)

        connection.connect()
        viewModel.selected.value shouldEqual id
    }

    @Test
    fun onSelectionsComputedShouldUpdateFilterState() {
        val viewModel = FilterMapViewModel(filters)
        val filterState = FilterState()
        val connection = viewModel.connectFilterState(filterState, groupID)

        connection.connect()
        viewModel.eventSelection.send(id)
        filterState shouldEqual expectedFilterState
    }

    @Test
    fun selectingTwiceShouldRemoveFilter() {
        val viewModel = FilterMapViewModel(filters)
        val filterState = FilterState()
        val connection = viewModel.connectFilterState(filterState, groupID)

        connection.connect()
        viewModel.eventSelection.send(id)
        viewModel.eventSelection.send(id)
        filterState.getFilters(groupID).shouldBeEmpty()
    }

    @Test
    fun onFilterStateChangedShouldUpdateSelections() {
        val viewModel = FilterMapViewModel(filters)
        val filterState = FilterState()
        val connection = viewModel.connectFilterState(filterState, groupID)

        connection.connect()
        filterState.notify { add(groupID, red) }
        viewModel.selected.value shouldEqual id
    }
}
