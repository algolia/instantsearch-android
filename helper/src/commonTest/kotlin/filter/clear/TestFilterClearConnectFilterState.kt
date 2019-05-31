package filter.clear

import com.algolia.instantsearch.helper.filter.clear.ClearMode
import com.algolia.instantsearch.helper.filter.clear.FilterClearViewModel
import com.algolia.instantsearch.helper.filter.clear.click
import com.algolia.instantsearch.helper.filter.clear.connectFilterState
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import shouldBeEmpty
import shouldEqual
import kotlin.test.Test


class TestFilterClearConnectFilterState {

    private val color = Attribute("color")
    private val red = Filter.Facet(color, "red")
    private val green = Filter.Facet(color, "green")
    private val groupIDA = FilterGroupID.Or("A")
    private val groupIDB = FilterGroupID.And("B")
    private val facetGroups = mutableMapOf(
        groupIDA to setOf(red),
        groupIDB to setOf(green)
    )

    @Test
    fun clearSpecifiedEmptyListShouldClearAll() {
        val viewModel = FilterClearViewModel()
        val filterState = FilterState(facetGroups = facetGroups)

        viewModel.connectFilterState(filterState, listOf(), ClearMode.Specified)
        viewModel.click()
        filterState.getFilters().shouldBeEmpty()
    }

    @Test
    fun clearSpecifiedShouldClearGroup() {
        val viewModel = FilterClearViewModel()
        val filterState = FilterState(facetGroups = facetGroups)

        viewModel.connectFilterState(filterState, listOf(groupIDA), ClearMode.Specified)
        viewModel.click()
        filterState shouldEqual FilterState(facetGroups = mutableMapOf(groupIDB to setOf(green)))
    }

    @Test
    fun clearExceptShouldClearGroup() {
        val viewModel = FilterClearViewModel()
        val filterState = FilterState(facetGroups = facetGroups)

        viewModel.connectFilterState(filterState, listOf(groupIDA), ClearMode.Except)
        viewModel.click()
        filterState shouldEqual FilterState(facetGroups = mutableMapOf(groupIDA to setOf(red)))
    }
}
