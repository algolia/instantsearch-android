package filter.clear

import com.algolia.instantsearch.filter.clear.ClearMode
import com.algolia.instantsearch.filter.clear.FilterClearViewModel
import com.algolia.instantsearch.filter.clear.connectFilterState
import com.algolia.instantsearch.filter.state.FilterGroupID
import com.algolia.instantsearch.filter.state.FilterOperator
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import kotlin.test.Test
import shouldBeEmpty
import shouldEqual

class TestFilterClearConnectFilterState {

    private val color = Attribute("color")
    private val red = Filter.Facet(color, "red")
    private val green = Filter.Facet(color, "green")
    private val groupIDA = FilterGroupID("A", FilterOperator.Or)
    private val groupIDB = FilterGroupID("B", FilterOperator.And)
    private val filters = mapOf(
        groupIDA to setOf(red),
        groupIDB to setOf(green)
    )

    @Test
    fun clearSpecifiedEmptyListShouldClearAll() {
        val viewModel = FilterClearViewModel()
        val filterState = FilterState(filters)
        val connection = viewModel.connectFilterState(filterState, listOf(), ClearMode.Specified)

        connection.connect()
        viewModel.eventClear.send(Unit)
        filterState.getFilters().shouldBeEmpty()
    }

    @Test
    fun clearSpecifiedShouldClearGroup() {
        val viewModel = FilterClearViewModel()
        val filterState = FilterState(filters)
        val connection = viewModel.connectFilterState(filterState, listOf(groupIDA), ClearMode.Specified)

        connection.connect()
        viewModel.eventClear.send(Unit)
        filterState shouldEqual FilterState(mapOf(groupIDB to setOf(green)))
    }

    @Test
    fun clearExceptShouldClearGroup() {
        val viewModel = FilterClearViewModel()
        val filterState = FilterState(filters)
        val connection = viewModel.connectFilterState(filterState, listOf(groupIDA), ClearMode.Except)

        connection.connect()
        viewModel.eventClear.send(Unit)
        filterState shouldEqual FilterState(mapOf(groupIDA to setOf(red)))
    }
}
