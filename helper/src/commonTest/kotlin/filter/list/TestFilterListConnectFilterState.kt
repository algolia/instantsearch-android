package filter.list

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
    private val filter = Filter.Facet(color, "red")
    private val groupID = FilterGroupID.Or(color)
    private val selections = setOf(filter)
    private val expectedFilterState = FilterState(facetGroups = mutableMapOf(groupID to setOf(filter)))

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
        viewModel.computeSelections(filter)
        filterState.filters shouldEqual expectedFilterState.filters
    }

    @Test
    fun onFilterStateChangedShouldUpdateSelections() {
        val viewModel = FilterListViewModel.Facet()
        val filterState = FilterState()

        viewModel.connectFilterState(filterState, groupID)
        filterState.notify { add(groupID, filter) }
        viewModel.selections shouldEqual selections
    }
}