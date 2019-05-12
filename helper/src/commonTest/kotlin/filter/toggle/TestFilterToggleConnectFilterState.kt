package filter.toggle

import com.algolia.instantsearch.helper.filter.state.FilterGroupID
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
    private val groupID = FilterGroupID.Or(color)
    private val expectedFilterState = FilterState(facetGroups = mutableMapOf(groupID to setOf(red)))

    @Test
    fun connectShouldUpdateIsSelectedWithFilterState() {
        val viewModel = FilterToggleViewModel(red)

        viewModel.connectFilterState(expectedFilterState)
        viewModel.isSelected shouldEqual true
    }

    @Test
    fun onSelectionsComputedShouldUpdateFilterState() {
        val viewModel = FilterToggleViewModel(red)
        val filterState = FilterState()

        viewModel.connectFilterState(filterState)
        viewModel.computeIsSelected(true)
        filterState.filters shouldEqual expectedFilterState.filters
    }

    @Test
    fun onFilterStateChangedShouldUpdateSelections() {
        val viewModel = FilterToggleViewModel(red)
        val filterState = FilterState()

        viewModel.connectFilterState(filterState)
        filterState.notify { add(groupID, red) }
        viewModel.isSelected shouldEqual true
    }
}