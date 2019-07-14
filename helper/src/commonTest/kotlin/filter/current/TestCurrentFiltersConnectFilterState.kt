package filter.current

import com.algolia.instantsearch.helper.filter.current.*
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
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
    private val filters = colorFilters + brandFilters
    private val identifiedFilters = filters.map { it.value.toString() to it }.toMap()
    private val filterMap = mapOf(colorID to colorFilters, brandID to brandFilters)

    @Test
    fun connectShouldUpdateItems() {
        val viewModel = CurrentFiltersViewModel(identifiedFilters)
        val filterState = FilterState(filterMap)

        viewModel.connectFilterState(filterState)
        viewModel.filters shouldEqual filters
    }

    @Test
    fun onFilterStateChangedShouldUpdateItems() {
        val viewModel = CurrentFiltersViewModel(identifiedFilters)
        val filterState = FilterState(filterMap)

        viewModel.connectFilterState(filterState)
        filterState.notify { remove(colorID, filterRed) }
        viewModel.filters shouldEqual setOf(filterGreen, filterAlgolia)
    }

    @Test
    fun onFilterStateChangedShouldUpdateRelevantItems() {
        val viewModel = CurrentFiltersViewModel(identifiedFilters)
        val filterState = FilterState(filterMap)

        viewModel.connectFilterState(filterState, colorID)
        filterState.notify { add(brandID, filterElastic) }
        viewModel.filters shouldEqual setOf(filterGreen, filterRed)
    }

    @Test
    fun onEventRemoveShouldUpdateFilterState() {
        val viewModel = CurrentFiltersViewModel(identifiedFilters)
        val filterState = FilterState(filterMap)

        viewModel.connectFilterState(filterState)
        viewModel.remove(filterIdentifier(colorID, filterRed))
        filterState.getFilters() shouldEqual setOf(filterGreen, filterAlgolia)
    }

    @Test
    fun onEventClearWithGroupIDShouldUpdateOnlyThatGroup() {
        val viewModel = CurrentFiltersViewModel(identifiedFilters)
        val filterState = FilterState(filterMap)

        viewModel.connectFilterState(filterState, colorID)
        viewModel.clear()
        filterState.getFilters() shouldEqual setOf(filterAlgolia)
    }

    @Test
    fun filterIdentifierWithAttribute() {
        val attribute = Attribute("foo")
        val groupID = FilterGroupID(attribute, FilterOperator.Or)
        val filter = Filter.Facet(attribute, "bar")

        groupFromIdentifier(filterIdentifier(groupID, filter)) shouldEqual groupID
    }

    @Test
    fun filterIdentifierWithEmptyName() {
        val groupID = FilterGroupID()
        val filter = Filter.Tag("bar")

        groupFromIdentifier(filterIdentifier(groupID, filter)) shouldEqual groupID
    }
}