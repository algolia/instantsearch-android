package refinement.facet

import blocking
import com.algolia.search.client.ClientSearch
import com.algolia.search.configuration.ConfigurationSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.FilterConverter
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.Facet
import filter.FilterGroupID
import filter.toFilter
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockHttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.io.ByteReadChannel
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import refinement.RefinementOperator
import search.SearcherSingleIndex
import shouldBeEmpty
import shouldEqual
import kotlin.test.Test


class TestRefinementFacetConnectors {

    private val color = Attribute("color")
    private val facets = listOf(
        Facet("blue", 1),
        Facet("red", 2),
        Facet("green", 3)
    )
    private val responseSearch = ResponseSearch(
        hitsOrNull = listOf(),
        facetsOrNull = mapOf(color to facets)
    )
    private val string = Json(JsonConfiguration.Stable.copy(encodeDefaults = false)).stringify(ResponseSearch.serializer(), responseSearch)
    private val mockEngine = MockEngine {
        MockHttpResponse(
            it.call,
            HttpStatusCode.OK,
            headers = headersOf("Content-Type", listOf(ContentType.Application.Json.toString())),
            content = ByteReadChannel(string)
        )
    }
    private val mockClient = ClientSearch(
        ConfigurationSearch(
            ApplicationID("A"),
            APIKey("B"),
            engine = mockEngine
        )
    )
    private val mockIndex = mockClient.initIndex(IndexName("index"))

    @Test
    fun connectWithSSI() {
        blocking {
            val searcher = SearcherSingleIndex(mockIndex)
            val model = RefinementFacetsViewModel()
            val facet = facets.first()
            val filter = facet.toFilter(color)

            model.connectSearcher(color, searcher, RefinementOperator.And)
            searcher.sequencer.currentOperation!!.join()
            model.items.toSet() shouldEqual facets.toSet()
            model.selections.shouldBeEmpty()
            model.selectItem(facet.value)
            searcher.sequencer.currentOperation!!.join()
            model.selections shouldEqual setOf(facet.value)
            searcher.query.filters = FilterConverter.SQL(filter)
            searcher.filterState.getFacets(FilterGroupID.And(color.raw))!! shouldEqual setOf(filter)
        }
    }

    @Test
    fun modelReactsToFilterStateChanges() {
        blocking {
            val searcher = SearcherSingleIndex(mockIndex)
            val model = RefinementFacetsViewModel()
            val facet = facets.first()

            model.connectSearcher(color, searcher, RefinementOperator.And)
            searcher.sequencer.currentOperation!!.join()
            model.selections.shouldBeEmpty()
            searcher.filterState.notify {
                add(FilterGroupID.And(color.raw), facet.toFilter(color))
            }
            model.selections shouldEqual setOf(facet.value)
        }
    }
}