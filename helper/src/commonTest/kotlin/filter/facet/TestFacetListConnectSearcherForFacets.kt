package filter.facet

import blocking
import com.algolia.instantsearch.helper.filter.facet.FacetListViewModel
import com.algolia.instantsearch.helper.filter.facet.connectSearcherForFacet
import com.algolia.instantsearch.helper.searcher.SearcherForFacets
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.response.ResponseSearchForFacets
import com.algolia.search.model.search.Facet
import mockClient
import respondJson
import shouldEqual
import kotlin.test.Test


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
    private val index = client.initIndex(IndexName("index"))

    @Test
    fun connectShouldUpdateItems() {
        val viewModel = FacetListViewModel()
        val searcher = SearcherForFacets(index, attribute).also { it.response = response }

        viewModel.connectSearcherForFacet(searcher)
        viewModel.item shouldEqual facets
    }

    @Test
    fun onResponseChangedShouldUpdateItems() {
        val viewModel = FacetListViewModel()
        val searcher = SearcherForFacets(index, attribute)

        viewModel.connectSearcherForFacet(searcher)
        blocking { searcher.search().join() }
        viewModel.item shouldEqual facets
    }
}