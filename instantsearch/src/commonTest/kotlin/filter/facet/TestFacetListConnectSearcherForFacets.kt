package filter.facet

import com.algolia.instantsearch.filter.facet.FacetListViewModel
import com.algolia.instantsearch.filter.facet.connectSearcherForFacet
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.response.ResponseSearchForFacets
import com.algolia.search.model.search.Facet
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import mockClient
import respondJson
import searcher.TestSearcherForFacets
import shouldEqual

class TestFacetListConnectSearcherForFacets {

    private val attribute = Attribute("color")
    private val red = Facet("red", 1)
    private val facets = listOf(red)
    private val response = ResponseSearchForFacets(
        facets = facets,
        exhaustiveFacetsCount = true,
        processingTimeMS = 0
    )
    private val client = mockClient(respondJson(response, ResponseSearchForFacets.serializer()))
    private val indexName = IndexName("index")

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
