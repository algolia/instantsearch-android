package refinement

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
import filter.toFilter
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockHttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.io.ByteReadChannel
import kotlinx.serialization.json.Json
import search.GroupID
import search.SearcherSingleIndex
import shouldBeEmpty
import shouldEqual
import kotlin.test.Test


class TestRefinementConnectors {

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
    private val string = Json(encodeDefaults = false).stringify(ResponseSearch.serializer(), responseSearch)
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
            val model = RefinementListViewModel<Facet>()
            val selection = facets.first()
            val selectedFilter = selection.toFilter(color)

            model.connect(color, searcher)
            searcher.search().join()
            model.refinements.toSet() shouldEqual facets.toSet()
            model.selections.shouldBeEmpty()
            model.select(selection)
            model.selections shouldEqual listOf(selection)
            searcher.query.filters = FilterConverter.SQL(selectedFilter)
            searcher.filterState.get().facet.getValue(GroupID.And(color.raw)) shouldEqual setOf(selectedFilter)
        }
    }

    @Test
    fun modelReactsToFilterStateChanges() {
        blocking {
            val searcher = SearcherSingleIndex(mockIndex)
            val model = RefinementListViewModel<Facet>()
            val selection = facets.first()

            model.connect(color, searcher)
            searcher.search().join()
            model.selections.shouldBeEmpty()
            searcher.filterState.add(GroupID.And(color.raw), selection.toFilter(color))
            model.selections shouldEqual listOf(selection)
        }
    }
}