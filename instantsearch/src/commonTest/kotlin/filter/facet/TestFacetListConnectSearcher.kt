package filter.facet

import com.algolia.instantsearch.filter.facet.FacetListViewModel
import com.algolia.instantsearch.filter.facet.connectSearcher
import com.algolia.client.model.search.SearchResponse
import com.algolia.instantsearch.filter.Facet
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import mockClient
import respondSearch
import searcher.TestSearcherSingle
import shouldEqual

class TestFacetListConnectSearcher {

    private val color = "color"
    private val red = Facet("red", 1)
    private val facets = listOf(red)
    private val response = SearchResponse(
        hits = emptyList(),
        query = "",
        params = "",
        facets = mapOf(color to mapOf(red.value to red.count))
    )
    private val client = mockClient(respondSearch(response))
    private val indexName = "index"

    @Test
    fun connectShouldSetQueryFacets() {
        val searcher = TestSearcherSingle(client, indexName)
        val viewModel = FacetListViewModel()
        val connection = viewModel.connectSearcher(searcher, color)

        connection.connect()
        searcher.query.facets!! shouldEqual listOf(color)
    }

    @Test
    fun connectShouldUpdateItems() {
        val searcher = TestSearcherSingle(client, indexName).also {
            it.response.value = response
        }
        val viewModel = FacetListViewModel()
        val connection = viewModel.connectSearcher(searcher, color)

        connection.connect()
        viewModel.items.value shouldEqual facets
    }

    @Test
    fun onResponseChangedShouldUpdateItems() = runTest {
        val searcher = TestSearcherSingle(client, indexName)
        val viewModel = FacetListViewModel()
        val connection = viewModel.connectSearcher(searcher, color)

        connection.connect()
        searcher.searchAsync().join()
        viewModel.items.value shouldEqual facets
    }
}
