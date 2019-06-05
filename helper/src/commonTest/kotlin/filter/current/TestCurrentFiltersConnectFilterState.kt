package filter.current

import com.algolia.instantsearch.helper.filter.current.CurrentFiltersViewModel
import com.algolia.instantsearch.helper.filter.current.connectFilterState
import com.algolia.instantsearch.helper.filter.current.filterIdentifier
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import shouldBeEmpty
import shouldEqual
import kotlin.test.Test


class TestCurrentFiltersConnectFilterState {

    private val red: String = "red"
    private val green: String = "green"
    private val color = Attribute("color")
    private val groupID = FilterGroupID(color)
    private val filterRed = Filter.Facet(color, red)
    private val filterGreen = Filter.Facet(color, green)
    private val filters = setOf(filterRed, filterGreen)
    private val identifiedFilters = filters.map { Pair(it.value.toString(), it) }.toMap()
    private val filterMap = mapOf(groupID to filters)

    @Test
    fun connectShouldUpdateItems() {
        val viewModel = CurrentFiltersViewModel(identifiedFilters)
        val filterState = FilterState(filterMap)

        viewModel.connectFilterState(filterState)
        viewModel.getValues() shouldEqual filters
    }

    @Test
    fun onFilterStateChangedShouldUpdateItems() {
        val viewModel = CurrentFiltersViewModel(identifiedFilters)
        val filterState = FilterState(filterMap)

        viewModel.connectFilterState(filterState)
        filterState.notify { remove(groupID, filterRed) }
        viewModel.getValues() shouldEqual setOf(filterGreen)
    }

    @Test
    fun onItemsChangedShouldUpdateFilterState() {
        val viewModel = CurrentFiltersViewModel()
        val filterState = FilterState()

        viewModel.connectFilterState(filterState)
        filterState.getFilters().shouldBeEmpty()
        viewModel.item = identifiedFilters
        viewModel.getValues() shouldEqual filters
    }

    @Test
    fun onTriggeredShouldUpdateFilterState() {
        val viewModel = CurrentFiltersViewModel(identifiedFilters)
        val filterState = FilterState(filterMap)

        viewModel.connectFilterState(filterState)
        viewModel.remove(filterIdentifier(groupID, filterRed))
        filterState.getFilters() shouldEqual setOf(filterGreen)
    }

}