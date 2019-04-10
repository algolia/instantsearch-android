package refinements

import blocking
import com.algolia.search.client.ClientSearch
import com.algolia.search.configuration.ConfigurationSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.Facet
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockHttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.io.ByteReadChannel
import kotlinx.serialization.json.Json
import refinement.RefinementListViewModel
import refinement.RefinementMode
import refinement.connectWith
import refinement.toFilter
import search.Group
import search.SearchFilterState
import search.SearcherSingleIndex
import shouldBeEmpty
import shouldEqual
import kotlin.test.Test

class TestRefinementConnectors {

    val color = Attribute("color")
    val facets = listOf(
        Facet("blue", 1),
        Facet("red", 2),
        Facet("green", 3)
    )

    val mockClient = ClientSearch(
        ConfigurationSearch(
            ApplicationID("A"),
            APIKey("B"),
            engine = MockEngine {
                MockHttpResponse(
                    it.call,
                    HttpStatusCode.OK,
                    content = ByteReadChannel(
                        Json.stringify(
                            ResponseSearch.serializer(),
                            ResponseSearch(facetsOrNull = mapOf(color to facets))
                        )
                    )
                )
            })
    )
    val mockIndex = mockClient.initIndex(IndexName("foo"))

    @Test
    fun connectWithSSI() {
        blocking {
            val searcher = SearcherSingleIndex(mockIndex)
            val model = RefinementListViewModel<Facet>()
            val searchFilterState = SearchFilterState()

            model.connectWith(
                searcher,
                searchFilterState,
                RefinementMode.Conjunctive,
                color
            )
            // WHEN
            searcher.search()
            searcher.completed?.await()
            // EXPECT
            model.refinements shouldEqual facets
            model.selected.shouldBeEmpty()

            // WHEN
            model.select(facets[0])

            searchFilterState.get().getValue(Group.And.Facet(color.raw)) shouldEqual setOf(facets[0].toFilter(color))

        }
    }
}