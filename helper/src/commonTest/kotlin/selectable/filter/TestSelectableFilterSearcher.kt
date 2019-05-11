package selectable.filter

import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.response.ResponseSearch
import filter.FilterGroupID
import filter.FilterState
import mockClient
import searcher.SearcherSingleIndex
import shouldEqual
import kotlin.test.Test


class TestSelectableFilterSearcher {

    private val response = ResponseSearch()
    private val client = mockClient(response, ResponseSearch.serializer())
    private val index = client.initIndex(IndexName("index"))
    private val color = Attribute("color")
    private val red = Filter.Facet(color, "red")
    private val groupID = FilterGroupID.Or(color)
    private val filterState = FilterState(facetGroups = mutableMapOf(groupID to setOf(red)))

    @Test
    fun connectShouldSetQueryFacets() {
        val searcher = SearcherSingleIndex(index)
        val viewModel = SelectableFilterViewModel(red)

        viewModel.connectSearcher(searcher)
        searcher.query.facets!! shouldEqual setOf(color)
    }

    @Test
    fun connectShouldUpdateIsSelectedWithFilterState() {
        val searcher = SearcherSingleIndex(index, filterState = filterState)
        val viewModel = SelectableFilterViewModel(red)

        viewModel.connectSearcher(searcher)
        viewModel.isSelected shouldEqual true
    }

    @Test
    fun onSelectionsComputedShouldUpdateFilterState() {
        val searcher = SearcherSingleIndex(index)
        val viewModel = SelectableFilterViewModel(red)

        viewModel.connectSearcher(searcher)
        viewModel.computeIsSelected(true)
        searcher.filterState.filters shouldEqual filterState.filters
    }

    @Test
    fun onFilterStateChangedShouldUpdateSelections() {
        val searcher = SearcherSingleIndex(index)
        val viewModel = SelectableFilterViewModel(red)

        viewModel.connectSearcher(searcher)
        searcher.filterState.notify { add(groupID, red) }
        viewModel.isSelected shouldEqual true
    }
}