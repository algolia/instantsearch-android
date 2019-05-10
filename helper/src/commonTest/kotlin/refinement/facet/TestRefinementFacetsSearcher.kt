package refinement.facet

import blocking
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.Facet
import filter.FilterGroupID
import filter.FilterState
import filter.toFilter
import mockClient
import search.SearcherSingleIndex
import shouldEqual
import kotlin.test.Test


class TestRefinementFacetsSearcher {

    private val color = Attribute("color")
    private val red = Facet("red", 1)
    private val facets = listOf(red)
    private val response = ResponseSearch(
        hitsOrNull = listOf(),
        facetsOrNull = mapOf(color to facets)
    )
    private val client = mockClient(response, ResponseSearch.serializer())
    private val index = client.initIndex(IndexName("index"))
    private val groupID = FilterGroupID.Or(color)
    private val selections = setOf(red.value)
    private val filterRed = red.toFilter(color)
    private val filterState = FilterState(facetGroups = mutableMapOf(groupID to setOf(filterRed)))

    @Test
    fun connectShouldSetQueryFacets() {
        val searcher = SearcherSingleIndex(index)
        val viewModel = RefinementFacetsViewModel()

        viewModel.connectSearcher(color, searcher)
        searcher.query.facets!! shouldEqual setOf(color)
    }

    @Test
    fun connectShouldUpdateSelectionsWithFilterState() {
        val searcher = SearcherSingleIndex(index, filterState = filterState)
        val viewModel = RefinementFacetsViewModel()

        viewModel.connectSearcher(color, searcher)
        viewModel.selections shouldEqual selections
    }

    @Test
    fun onSelectionsComputedShouldUpdateFilterState() {
        val searcher = SearcherSingleIndex(index)
        val viewModel = RefinementFacetsViewModel()

        viewModel.connectSearcher(color, searcher)
        viewModel.computeSelections(red.value)
        searcher.filterState.filters shouldEqual filterState.filters
    }

    @Test
    fun onFilterStateChangedShouldUpdateSelections() {
        val searcher = SearcherSingleIndex(index)
        val viewModel = RefinementFacetsViewModel()

        viewModel.connectSearcher(color, searcher)
        searcher.filterState.notify { add(groupID, filterRed) }
        viewModel.selections shouldEqual selections
    }

    @Test
    fun onResponseChangedShouldUpdateItems() {
        val searcher  = SearcherSingleIndex(index)
        val viewModel = RefinementFacetsViewModel()

        viewModel.connectSearcher(color, searcher)
        searcher.search()
        blocking { searcher.sequencer.currentOperationOrNull?.join() }
        viewModel.items shouldEqual facets
    }
}