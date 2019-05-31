package filter.clear

import com.algolia.instantsearch.helper.filter.clear.FilterClearViewModel
import com.algolia.instantsearch.helper.filter.clear.click
import com.algolia.instantsearch.helper.filter.clear.connectFilterState
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import shouldEqual
import kotlin.test.Test


class TestFilterClearConnectFilterState {

    private val color = Attribute("color")
    private val red = Filter.Facet(color, "red")
    private val green = Filter.Facet(color, "green")
    private val groupID = FilterGroupID.Or(color)
    private val otherGroupID = FilterGroupID.And(color)

    @Test
    fun testClearAll() {
        val filterState = FilterState(facetGroups = mutableMapOf(groupID to setOf(red)))
        val viewModel = FilterClearViewModel()
        viewModel.connectFilterState(filterState)
        viewModel.click()
        filterState.getFilters() shouldEqual setOf()
    }

    @Test
    fun testClearExistingGroup() {
        val filterState = FilterState(
            facetGroups = mutableMapOf(
                groupID to setOf(red),
                otherGroupID to setOf(green)
            )
        )
        val viewModel = FilterClearViewModel()
        viewModel.connectFilterState(filterState, groupID)
        viewModel.click()
        filterState.getFilters() shouldEqual setOf(green)
    }

    @Test
    fun testClearMissingGroup() {
        val filterState = FilterState(facetGroups = mutableMapOf(groupID to setOf(red)))
        val viewModel = FilterClearViewModel()
        viewModel.connectFilterState(filterState, otherGroupID)
        viewModel.click()
        filterState.getFilters() shouldEqual setOf(red)
    }

    @Test
    fun testClearExceptExistingGroup() {
        val filterState = FilterState(
            facetGroups = mutableMapOf(
                groupID to setOf(red),
                otherGroupID to setOf(green)
            )
        )
        val viewModel = FilterClearViewModel()
        viewModel.connectFilterState(filterState, true, groupID)
        viewModel.click()
        filterState.getFilters() shouldEqual setOf(red)
    }

    @Test
    fun testClearExceptMissingGroup() {
        val filterState = FilterState(facetGroups = mutableMapOf(groupID to setOf(red)))
        val viewModel = FilterClearViewModel()
        viewModel.connectFilterState(filterState, true, otherGroupID)
        viewModel.click()
        filterState.getFilters() shouldEqual setOf()
    }
}
