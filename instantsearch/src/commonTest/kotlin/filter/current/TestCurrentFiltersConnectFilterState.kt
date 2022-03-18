package filter.current

import com.algolia.instantsearch.filter.current.FilterCurrentViewModel
import com.algolia.instantsearch.filter.current.connectFilterState
import com.algolia.instantsearch.filter.state.FilterGroupID
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import shouldEqual
import kotlin.test.Test

class TestCurrentFiltersConnectFilterState {

    private val color = Attribute("color")
    private val brand = Attribute("brand")
    private val colorID = FilterGroupID(color)
    private val brandID = FilterGroupID(brand)
    private val filterRed = Filter.Facet(color, "red")
    private val filterGreen = Filter.Facet(color, "green")
    private val filterAlgolia = Filter.Facet(brand, "algolia")
    private val filterElastic = Filter.Facet(brand, "elastic")

    private val colorFilters = setOf(filterRed, filterGreen)
    private val brandFilters = setOf(filterAlgolia)
    private val filters = mapOf(colorID to colorFilters, brandID to brandFilters)
    private val filterAndIDs = filters.toFilterAndIds()

    @Test
    fun connectShouldUpdateItems() {
        val viewModel = FilterCurrentViewModel(filterAndIDs)
        val filterState = FilterState(filters)
        val connection = viewModel.connectFilterState(filterState)

        connection.connect()
        viewModel.map.value shouldEqual filterAndIDs
    }

    @Test
    fun onFilterStateChangedShouldUpdateItems() {
        val viewModel = FilterCurrentViewModel(filterAndIDs)
        val filterState = FilterState(filters)
        val connection = viewModel.connectFilterState(filterState)

        connection.connect()
        filterState.notify { remove(colorID, filterRed) }
        viewModel.map.value shouldEqual mapOf(
            (colorID to filterGreen) to filterGreen,
            (brandID to filterAlgolia) to filterAlgolia
        )
    }

    @Test
    fun onFilterStateChangedShouldUpdateRelevantItems() {
        val viewModel = FilterCurrentViewModel(filterAndIDs)
        val filterState = FilterState(filters)
        val connection = viewModel.connectFilterState(filterState, listOf(colorID))

        connection.connect()
        filterState.notify { add(brandID, filterElastic) }
        viewModel.map.value shouldEqual mapOf(
            (colorID to filterGreen) to filterGreen,
            (colorID to filterRed) to filterRed
        )
    }

    @Test
    fun onEventRemoveShouldUpdateFilterState() {
        val viewModel = FilterCurrentViewModel(filterAndIDs)
        val filterState = FilterState(filters)
        val connection = viewModel.connectFilterState(filterState)

        connection.connect()
        viewModel.remove(colorID to filterRed)
        filterState.getFilters() shouldEqual setOf(filterGreen, filterAlgolia)
    }

    @Test
    fun onEventClearWithGroupIDShouldUpdateOnlyThatGroup() {
        val viewModel = FilterCurrentViewModel(filterAndIDs)
        val filterState = FilterState(filters)
        val connection = viewModel.connectFilterState(filterState, listOf(colorID))

        connection.connect()
        viewModel.clear()
        filterState.getFilters() shouldEqual setOf(filterAlgolia)
    }
}
