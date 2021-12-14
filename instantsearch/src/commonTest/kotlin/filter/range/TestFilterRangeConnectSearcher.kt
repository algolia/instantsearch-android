package filter.range

import com.algolia.instantsearch.core.number.range.NumberRangeViewModel
import com.algolia.instantsearch.helper.filter.range.connectSearcher
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.hits.HitsSearcher
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.FacetStats
import mockClient
import shouldBeTrue
import shouldEqual
import kotlin.test.Test

class TestFilterRangeConnectSearcher {

    private val client = mockClient()
    private val attribute = Attribute("foo")

    @Test
    fun onConnectUpdateQueryFacets() {
        val viewModel = NumberRangeViewModel<Int>()
        val searcher = HitsSearcher(client, IndexName("Index"))
        val connection = viewModel.connectSearcher(searcher, attribute)
        connection.connect()

        val facets = searcher.query.facets!!
        facets.contains(attribute).shouldBeTrue()
    }

    @Test
    fun onSearchResultUpdateBounds() {
        val viewModel = NumberRangeViewModel<Int>()
        val searcher =  HitsSearcher(client, IndexName("Index"))
        val connection = viewModel.connectSearcher(searcher, attribute)
        connection.connect()

        val facetStats = FacetStats(min = 1f, max = 10f, average = 5f, sum = 15f)
        val attributeFacetStats: Map<Attribute, FacetStats> = mapOf(attribute to facetStats)
        searcher.response.value = ResponseSearch(facetStatsOrNull = attributeFacetStats)

        val bounds = viewModel.bounds.value!!
        bounds.min shouldEqual facetStats.min.toInt()
        bounds.max shouldEqual facetStats.max.toInt()
    }

    @Test
    fun onSearchResultUpdateBoundsCustom() {
        val viewModel = NumberRangeViewModel<Double>()
        val searcher =  HitsSearcher(client, IndexName("Index"))
        val connection = viewModel.connectSearcher(searcher, attribute) { it.toDouble() }
        connection.connect()

        val facetStats = FacetStats(min = 1f, max = 10f, average = 5f, sum = 15f)
        val attributeFacetStats: Map<Attribute, FacetStats> = mapOf(attribute to facetStats)
        searcher.response.value = ResponseSearch(facetStatsOrNull = attributeFacetStats)

        val bounds = viewModel.bounds.value!!
        bounds.min shouldEqual facetStats.min.toDouble()
        bounds.max shouldEqual facetStats.max.toDouble()
    }
}
