package filter.facet

import com.algolia.client.model.search.FacetHits
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.filter.facet.FacetListViewModel
import com.algolia.instantsearch.filter.facet.connectFilterState
import com.algolia.instantsearch.filter.state.FilterGroupID
import com.algolia.instantsearch.filter.state.FilterOperator
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.filter.state.toFilter
import shouldEqual
import kotlin.test.Test

class TestFacetListConnectFilterState {

    private val color = "color"
    private val red = FacetHits("red", "", 1)
    private val green = FacetHits("green", "", 2)
    private val groupID = FilterGroupID(color, FilterOperator.Or)
    private val selections = setOf(red.value)
    private val filterRed = red.toFilter(color)
    private val filterGreen = green.toFilter(color)
    private val filters = mapOf(groupID to setOf(filterRed, filterGreen))
    private val expectedFilterState = FilterState(mapOf(groupID to setOf(filterRed)))

    @Test
    fun connectShouldUpdateSelectionsWithFilterState() {
        val viewModel = FacetListViewModel()
        val connection = viewModel.connectFilterState(expectedFilterState, color, groupID)

        connection.connect()
        viewModel.selections.value shouldEqual selections
    }

    @Test
    fun onSelectionsComputedShouldUpdateFilterState() {
        val viewModel = FacetListViewModel()
        val filterState = FilterState()
        val connection = viewModel.connectFilterState(filterState, color, groupID)

        connection.connect()
        viewModel.select(red.value)
        filterState shouldEqual expectedFilterState
    }

    @Test
    fun onFilterStateChangedShouldUpdateSelections() {
        val viewModel = FacetListViewModel()
        val filterState = FilterState()
        val connection = viewModel.connectFilterState(filterState, color, groupID)

        connection.connect()
        filterState.notify { add(groupID, filterRed) }
        viewModel.selections.value shouldEqual selections
    }

    @Test
    fun selectionModeSingleShouldClearOtherFilters() {
        val filterState = FilterState(filters)
        val viewModel = FacetListViewModel(selectionMode = SelectionMode.Single).apply {
            items.value = listOf(red)
        }
        val connection = viewModel.connectFilterState(filterState, color, groupID)

        connection.connect()
        viewModel.selections.value shouldEqual setOf(red.value, green.value)
        viewModel.select(red.value)
        filterState shouldEqual FilterState()
    }

    @Test
    fun selectionModeMultipleShouldNotClearOtherFilters() {
        val filterState = FilterState(filters)
        val viewModel = FacetListViewModel(selectionMode = SelectionMode.Multiple).apply {
            items.value = listOf(red)
        }
        val connection = viewModel.connectFilterState(filterState, color, groupID)

        connection.connect()
        viewModel.selections.value shouldEqual setOf(red.value, green.value)
        viewModel.select(red.value)
        filterState shouldEqual FilterState(mapOf(groupID to setOf(filterGreen)))
    }

    @Test
    fun facetPersistentSelection() {
        val filterState = FilterState(filters)
        val viewModel = FacetListViewModel(selectionMode = SelectionMode.Multiple, persistentSelection = true).apply {
            items.value = listOf(red)
        }
        val connection = viewModel.connectFilterState(filterState, color, groupID)

        connection.connect()
        viewModel.selections.value shouldEqual setOf(red.value, green.value)
        viewModel.select(green.value)
        viewModel.selections.value shouldEqual setOf(red.value)
    }
}
