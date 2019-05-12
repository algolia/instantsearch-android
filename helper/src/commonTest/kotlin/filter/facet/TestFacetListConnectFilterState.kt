package filter.facet

import com.algolia.instantsearch.helper.filter.facet.FacetListViewModel
import com.algolia.instantsearch.helper.filter.facet.connectFilterState
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.toFilter
import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Facet
import shouldEqual
import kotlin.test.Test


class TestFacetListConnectFilterState {

    private val color = Attribute("color")
    private val red = Facet("red", 1)
    private val groupID = FilterGroupID.Or(color)
    private val selections = setOf(red.value)
    private val filterRed = red.toFilter(color)
    private val expectedFilterState = FilterState(facetGroups = mutableMapOf(groupID to setOf(filterRed)))

    @Test
    fun connectShouldUpdateSelectionsWithFilterState() {
        val viewModel = FacetListViewModel()

        viewModel.connectFilterState(color, expectedFilterState)
        viewModel.selections shouldEqual selections
    }

    @Test
    fun onSelectionsComputedShouldUpdateFilterState() {
        val viewModel = FacetListViewModel()
        val filterState = FilterState()

        viewModel.connectFilterState(color, filterState)
        viewModel.computeSelections(red.value)
        filterState.filters shouldEqual expectedFilterState.filters
    }

    @Test
    fun onFilterStateChangedShouldUpdateSelections() {
        val viewModel = FacetListViewModel()
        val filterState = FilterState()

        viewModel.connectFilterState(color, filterState)
        filterState.notify { add(groupID, filterRed) }
        viewModel.selections shouldEqual selections
    }
}