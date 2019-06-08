package filter.facet

import blocking
import com.algolia.instantsearch.helper.filter.facet.FacetListViewModel
import com.algolia.instantsearch.helper.filter.facet.connectSearcher
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.Facet
import mockClient
import respondSearch
import shouldEqual
import kotlin.test.Test


class TestFacetListConnectSearcher {

    private val color = Attribute("color")
    private val red = Facet("red", 1)
    private val facets = listOf(red)
    private val response = ResponseSearch(facetsOrNull = mapOf(color to facets))
    private val client = mockClient(respondSearch(response))
    private val index = client.initIndex(IndexName("index"))

    @Test
    fun connectShouldSetQueryFacets() {
        val searcher = SearcherSingleIndex(index)
        val viewModel = FacetListViewModel()

        viewModel.connectSearcher(color, searcher)
        searcher.query.facets!! shouldEqual setOf(color)
    }

    @Test
    fun connectShouldUpdateItems() {
        val searcher = SearcherSingleIndex(index).also { it.response = response }
        val viewModel = FacetListViewModel()

        viewModel.connectSearcher(color, searcher)
        viewModel.item shouldEqual facets
    }

    @Test
    fun onResponseChangedShouldUpdateItems() {
        val searcher = SearcherSingleIndex(index)
        val viewModel = FacetListViewModel()

        viewModel.connectSearcher(color, searcher)
        blocking { searcher.search() }
        viewModel.item shouldEqual facets
    }
}