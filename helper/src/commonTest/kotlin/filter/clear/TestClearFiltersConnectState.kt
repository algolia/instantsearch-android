package filter.clear

import com.algolia.instantsearch.helper.filter.clear.ClearFiltersViewModel
import com.algolia.instantsearch.helper.filter.clear.connectFilterState
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import shouldEqual
import kotlin.test.Test


class TestClearFiltersConnectState {

    private val color = Attribute("color")
    private val facetRed = Filter.Facet(color, "red")
    private val groupID = FilterGroupID.And(color)

    @Test
    fun connectShouldClearFiltersOnClear() {
        val viewModel = ClearFiltersViewModel()
        val filterState = FilterState(facetGroups = mutableMapOf(groupID to setOf(facetRed)))

        viewModel.connectFilterState(filterState)
        viewModel.clearFilters()
        filterState.getFilters() shouldEqual setOf()
    }
}