package refinement.filters

import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.response.ResponseSearch
import filter.FilterGroupID
import filter.FilterState
import mockClient
import search.SearcherSingleIndex
import shouldEqual
import kotlin.test.Test


class TestRefinementFiltersSearcher {

    private val response = ResponseSearch()
    private val client = mockClient(response, ResponseSearch.serializer())
    private val index = client.initIndex(IndexName("index"))
    private val color = Attribute("color")
    private val groupID = FilterGroupID.Or(color)
    private val red = Filter.Facet(color, "red")
    private val id = 0
    private val filters = mapOf(id to red)
    private val filterState = FilterState(facetGroups = mutableMapOf(groupID to setOf(red)))

    @Test
    fun connectShouldSetQueryFacets() {
        val searcher = SearcherSingleIndex(index)
        val viewModel = RefinementFiltersViewModel(filters)

        viewModel.connectSearcher(color, searcher)
        searcher.query.facets!! shouldEqual setOf(color)
    }

    @Test
    fun connectShouldUpdateSelectedWithFilterState() {
        val searcher = SearcherSingleIndex(index, filterState = filterState)
        val viewModel = RefinementFiltersViewModel(filters)

        viewModel.connectSearcher(color, searcher)
        viewModel.selected shouldEqual id
    }

    @Test
    fun onSelectionsComputedShouldUpdateFilterState() {
        val searcher = SearcherSingleIndex(index)
        val viewModel = RefinementFiltersViewModel(filters)

        viewModel.connectSearcher(color, searcher)
        viewModel.computeSelected(id)
        searcher.filterState.filters shouldEqual filterState.filters
    }

    @Test
    fun onFilterStateChangedShouldUpdateSelections() {
        val searcher = SearcherSingleIndex(index)
        val viewModel = RefinementFiltersViewModel(filters)

        viewModel.connectSearcher(color, searcher)
        searcher.filterState.notify { add(groupID, red) }
        viewModel.selected shouldEqual id
    }
}