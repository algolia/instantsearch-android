package filter.facet

import com.algolia.instantsearch.filter.facet.FacetListViewModel
import com.algolia.instantsearch.filter.facet.connectSearcherForFacet
import com.algolia.client.model.search.FacetHits
import com.algolia.client.model.search.SearchForFacetValuesResponse
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import mockClient
import respondJson
import searcher.TestSearcherForFacets
import shouldEqual

class TestFacetListConnectSearcherForFacets {

    private val attribute = "color"
    private val red = FacetHits("red", "", 1)
    private val facets = listOf(red)
    private val response = SearchForFacetValuesResponse(
        facetHits = listOf(FacetHits(value = red.value, highlighted = red.value, count = red.count)),
        exhaustiveFacetsCount = true,
        processingTimeMS = 0
    )
    private val client = mockClient(respondJson(response, SearchForFacetValuesResponse.serializer()))
    private val indexName = "index"

    @Test
    fun connectShouldUpdateItems() {
        val viewModel = FacetListViewModel()
        val searcher = TestSearcherForFacets(client, indexName, attribute).also {
            it.response.value = response
        }
        val connection = viewModel.connectSearcherForFacet(searcher)

        connection.connect()
        viewModel.items.value shouldEqual facets
    }

    @Test
    fun onResponseChangedShouldUpdateItems() = runTest {
        val viewModel = FacetListViewModel()
        val searcher = TestSearcherForFacets(client, indexName, attribute)
        val connection = viewModel.connectSearcherForFacet(searcher)

        connection.connect()
        searcher.searchAsync().join()
        viewModel.items.value shouldEqual facets
    }
}
