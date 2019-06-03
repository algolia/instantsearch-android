package filter.facet

import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.helper.filter.facet.FacetListViewModel
import com.algolia.instantsearch.helper.filter.facet.connectFilterState
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.toFilter
import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Facet
import shouldEqual
import kotlin.test.Test


class TestFacetListConnectFilterState {

    private val color = Attribute("color")
    private val red = Facet("red", 1)
    private val green = Facet("green", 2)
    private val groupID = FilterGroupID(color, FilterOperator.Or)
    private val selections = setOf(red.value)
    private val filterRed = red.toFilter(color)
    private val filterGreen = green.toFilter(color)
    private val filters = mapOf(groupID to setOf(filterRed, filterGreen))
    private val expectedFilterState = FilterState(mapOf(groupID to setOf(filterRed)))

    @Test
    fun connectShouldUpdateSelectionsWithFilterState() {
        val viewModel = FacetListViewModel()

        viewModel.connectFilterState(color, expectedFilterState, groupID)
        viewModel.selections shouldEqual selections
    }

    @Test
    fun onSelectionsComputedShouldUpdateFilterState() {
        val viewModel = FacetListViewModel()
        val filterState = FilterState()

        viewModel.connectFilterState(color, filterState, groupID)
        viewModel.computeSelections(red.value)
        filterState shouldEqual expectedFilterState
    }

    @Test
    fun onFilterStateChangedShouldUpdateSelections() {
        val viewModel = FacetListViewModel()
        val filterState = FilterState()

        viewModel.connectFilterState(color, filterState, groupID)
        filterState.notify { add(groupID, filterRed) }
        viewModel.selections shouldEqual selections
    }

    @Test
    fun selectionModeSingleShouldClearOtherFilters() {
        val filterState = FilterState(filters)
        val viewModel = FacetListViewModel(selectionMode = SelectionMode.Single).apply {
            item = listOf(red)
        }

        viewModel.connectFilterState(color, filterState, groupID)
        viewModel.selections shouldEqual setOf(red.value, green.value)
        viewModel.computeSelections(red.value)
        filterState shouldEqual FilterState()
    }

    @Test
    fun selectionModeMultipleShouldNotClearOtherFilters() {
        val filterState = FilterState(filters)
        val viewModel = FacetListViewModel(selectionMode = SelectionMode.Multiple).apply {
            item = listOf(red)
        }

        viewModel.connectFilterState(color, filterState, groupID)
        viewModel.selections shouldEqual setOf(red.value, green.value)
        viewModel.computeSelections(red.value)
        filterState shouldEqual FilterState(mapOf(groupID to setOf(filterGreen)))
    }

    @Test
    fun facetPersistentSelection() {
        val filterState = FilterState(filters)
        val viewModel = FacetListViewModel(selectionMode = SelectionMode.Multiple, persistentSelection = true).apply {
            item = listOf(red)
        }

        viewModel.connectFilterState(color, filterState, groupID)
        viewModel.selections shouldEqual setOf(red.value, green.value)
        viewModel.computeSelections(green.value)
        viewModel.selections shouldEqual setOf(red.value)
    }
}