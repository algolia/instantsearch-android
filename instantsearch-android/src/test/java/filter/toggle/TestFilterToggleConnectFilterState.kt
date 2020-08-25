package filter.toggle

import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.toggle.FilterToggleViewModel
import com.algolia.instantsearch.helper.filter.toggle.connectFilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import shouldEqual
import kotlin.test.Test


class TestFilterToggleConnectFilterState {

    private val color = Attribute("color")
    private val red = Filter.Facet(color, "red")
    private val groupID = FilterGroupID(color, FilterOperator.And)
    private val expectedFilterState = FilterState(mapOf(groupID to setOf(red)))

    @Test
    fun connectShouldUpdateIsSelectedWithFilterState() {
        val viewModel = FilterToggleViewModel(red)
        val connection = viewModel.connectFilterState(expectedFilterState, groupID = groupID)

        connection.connect()
        viewModel.isSelected.value shouldEqual true
    }

    @Test
    fun onSelectionsComputedShouldUpdateFilterState() {
        val viewModel = FilterToggleViewModel(red)
        val filterState = FilterState()
        val connection = viewModel.connectFilterState(filterState, groupID = groupID)

        connection.connect()
        viewModel.eventSelection.send(true)
        filterState shouldEqual expectedFilterState
    }

    @Test
    fun onFilterStateChangedShouldUpdateSelections() {
        val viewModel = FilterToggleViewModel(red)
        val filterState = FilterState()
        val connection = viewModel.connectFilterState(filterState, groupID = groupID)

        connection.connect()
        filterState.notify { add(groupID, red) }
        viewModel.isSelected.value shouldEqual true
    }
}