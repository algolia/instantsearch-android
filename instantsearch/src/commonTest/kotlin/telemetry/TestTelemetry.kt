package telemetry

import com.algolia.instantsearch.ExperimentalInstantSearch
import com.algolia.instantsearch.Internal
import com.algolia.instantsearch.core.internal.GlobalTelemetry
import com.algolia.instantsearch.helper.loading.LoadingConnector
import com.algolia.instantsearch.helper.searcher.hits.HitsSearcher
import com.algolia.instantsearch.telemetry.ComponentType
import com.algolia.search.model.IndexName
import mockClient
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalInstantSearch::class, Internal::class)
class TestTelemetry {

    val client = mockClient()
    val indexName = IndexName("myIndex")

    @Test
    fun testLoading() {
        val searcher = HitsSearcher(client, indexName)
        LoadingConnector(searcher)
        val components = GlobalTelemetry.schema().components.filter { it.type == ComponentType.Loading }
        assertEquals(components.size, 1)
        assertEquals(components.first().parameters, emptyList())
    }
}
