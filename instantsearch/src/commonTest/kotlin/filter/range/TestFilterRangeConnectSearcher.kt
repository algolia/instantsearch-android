package filter.range

import com.algolia.instantsearch.core.number.range.NumberRangeViewModel
import com.algolia.instantsearch.filter.range.connectSearcher
import com.algolia.client.model.search.FacetStats
import com.algolia.client.model.search.SearchResponse
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import mockClient
import shouldBeTrue
import shouldEqual
import kotlin.test.Test

class TestFilterRangeConnectSearcher {

    private val client = mockClient()
    private val attribute = "foo"

    @Test
    fun onConnectUpdateQueryFacets() {
        val viewModel = NumberRangeViewModel<Int>()
        val searcher = HitsSearcher(client, "Index")
        val connection = viewModel.connectSearcher(searcher, attribute)
        connection.connect()

        val facets = searcher.query.facets!!
        facets.contains(attribute).shouldBeTrue()
    }

    @Test
    fun onSearchResultUpdateBounds() {
        val viewModel = NumberRangeViewModel<Int>()
        val searcher = HitsSearcher(client, "Index")
        val connection = viewModel.connectSearcher(searcher, attribute)
        connection.connect()

        val facetStats = FacetStats(min = 1.0, max = 10.0, avg = 5.0, sum = 15.0)
        val attributeFacetStats: Map<String, FacetStats> = mapOf(attribute to facetStats)
        searcher.response.value = SearchResponse(
            hits = emptyList(),
            query = "",
            params = "",
            facetsStats = attributeFacetStats
        )

        val bounds = viewModel.bounds.value!!
        bounds.min shouldEqual facetStats.min!!.toInt()
        bounds.max shouldEqual facetStats.max!!.toInt()
    }

    @Test
    fun onSearchResultUpdateBoundsCustom() {
        val viewModel = NumberRangeViewModel<Double>()
        val searcher = HitsSearcher(client, "Index")
        val connection = viewModel.connectSearcher(searcher, attribute) { it.toDouble() }
        connection.connect()

        val facetStats = FacetStats(min = 1.0, max = 10.0, avg = 5.0, sum = 15.0)
        val attributeFacetStats: Map<String, FacetStats> = mapOf(attribute to facetStats)
        searcher.response.value = SearchResponse(
            hits = emptyList(),
            query = "",
            params = "",
            facetsStats = attributeFacetStats
        )

        val bounds = viewModel.bounds.value!!
        bounds.min shouldEqual facetStats.min!!.toDouble()
        bounds.max shouldEqual facetStats.max!!.toDouble()
    }
}
