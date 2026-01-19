package filter.list

import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.filter.list.FilterListViewModel
import com.algolia.instantsearch.filter.list.connectFilterState
import com.algolia.instantsearch.filter.state.FilterGroupID
import com.algolia.instantsearch.filter.state.FilterOperator
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import shouldEqual
import kotlin.test.Test

class TestFilterListConnectFilterState {

    private val color = String("color")
    private val red = Filter.Facet(color, "red")
    private val green = Filter.Facet(color, "green")
    private val groupID = FilterGroupID(color, FilterOperator.Or)
    private val selections = setOf(red)
    private val filters = mapOf(groupID to setOf(red, green))
    private val expectedFilterState = FilterState(mapOf(groupID to setOf(red)))

    @Test
    fun connectShouldUpdateSelectionsWithFilterState() {
        val viewModel = FilterListViewModel.Facet()
        val connection = viewModel.connectFilterState(expectedFilterState, groupID)

        connection.connect()
        viewModel.selections.value shouldEqual selections
    }

    @Test
    fun onSelectionsComputedShouldUpdateFilterState() {
        val viewModel = FilterListViewModel.Facet()
        val filterState = FilterState()
        val connection = viewModel.connectFilterState(filterState, groupID)

        connection.connect()
        viewModel.select(red)
        filterState shouldEqual expectedFilterState
    }

    @Test
    fun onFilterStateChangedShouldUpdateSelections() {
        val viewModel = FilterListViewModel.Facet()
        val filterState = FilterState()
        val connection = viewModel.connectFilterState(filterState, groupID)

        connection.connect()
        filterState.notify { add(groupID, red) }
        viewModel.selections.value shouldEqual selections
    }

    @Test
    fun selectionModeSingleShouldClearOtherFilters() {
        val filterState = FilterState(filters)
        val viewModelFacets = FilterListViewModel.Facet(selectionMode = SelectionMode.Single).apply {
            items.value = listOf(red)
        }
        val connection = viewModelFacets.connectFilterState(filterState, groupID)

        connection.connect()
        viewModelFacets.selections.value shouldEqual setOf(red, green)
        viewModelFacets.select(red)
        filterState shouldEqual FilterState()
    }

    @Test
    fun selectionModeMultipleShouldNotClearOtherFilters() {
        val filterState = FilterState(filters)
        val viewModelFacets = FilterListViewModel.Facet(selectionMode = SelectionMode.Multiple).apply {
            items.value = listOf(red)
        }
        val connection = viewModelFacets.connectFilterState(filterState, groupID)

        connection.connect()
        viewModelFacets.selections.value shouldEqual setOf(red, green)
        viewModelFacets.select(red)
        filterState shouldEqual FilterState(mapOf(groupID to setOf(green)))
    }
}
