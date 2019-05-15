package filter.list

import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.helper.filter.list.FilterListViewModel
import com.algolia.instantsearch.helper.filter.list.connectFilterState
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import shouldEqual
import kotlin.test.Test


class TestFilterListConnectFilterState {

    private val color = Attribute("color")
    private val red = Filter.Facet(color, "red")
    private val green = Filter.Facet(color, "green")
    private val groupID = FilterGroupID.Or(color)
    private val selections = setOf(red)
    private val expectedFilterState = FilterState(facetGroups = mutableMapOf(groupID to setOf(red)))

    @Test
    fun connectShouldUpdateSelectionsWithFilterState() {
        val viewModel = FilterListViewModel.Facet()

        viewModel.connectFilterState(expectedFilterState, groupID)
        viewModel.selections shouldEqual selections
    }

    @Test
    fun onSelectionsComputedShouldUpdateFilterState() {
        val viewModel = FilterListViewModel.Facet()
        val filterState = FilterState()

        viewModel.connectFilterState(filterState, groupID)
        viewModel.computeSelections(red)
        filterState shouldEqual expectedFilterState
    }

    @Test
    fun onFilterStateChangedShouldUpdateSelections() {
        val viewModel = FilterListViewModel.Facet()
        val filterState = FilterState()

        viewModel.connectFilterState(filterState, groupID)
        filterState.notify { add(groupID, red) }
        viewModel.selections shouldEqual selections
    }

    @Test
    fun selectionModeSingleShouldClearOtherFilters() {
        val filterState = FilterState(
            facetGroups = mutableMapOf(
                groupID to setOf(red, green)
            )
        )
        val viewModelFacets = FilterListViewModel.Facet(selectionMode = SelectionMode.Single).apply {
            items = listOf(red)
        }

        viewModelFacets.connectFilterState(filterState, groupID)
        viewModelFacets.selections shouldEqual setOf(red, green)
        viewModelFacets.computeSelections(red)
        filterState shouldEqual FilterState()
    }

    @Test
    fun selectionModeMultipleShouldNotClearOtherFilters() {
        val filterState = FilterState(
            facetGroups = mutableMapOf(
                groupID to setOf(red, green)
            )
        )
        val viewModelFacets = FilterListViewModel.Facet(selectionMode = SelectionMode.Multiple).apply {
            items = listOf(red)
        }

        viewModelFacets.connectFilterState(filterState, groupID)
        viewModelFacets.selections shouldEqual setOf(red, green)
        viewModelFacets.computeSelections(red)
        filterState shouldEqual FilterState(
            facetGroups = mutableMapOf(
                groupID to setOf(green)
            )
        )
    }
}