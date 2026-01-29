package searcher

import com.algolia.instantsearch.searcher.SearcherScope
import com.algolia.instantsearch.searcher.facets.FacetsSearcher
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.client.api.InsightsClient
import com.algolia.client.api.SearchClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

val TestCoroutineScope = SearcherScope(Dispatchers.Default)

fun TestSearcherSingle(
    client: SearchClient,
    indexName: String,
    insights: InsightsClient = InsightsClient(client.appId, client.apiKey),
    coroutineScope: CoroutineScope = TestCoroutineScope,
    isAutoSendingHitsViewEvents: Boolean = false,
) = HitsSearcher(
    client = client,
    insights = insights,
    indexName = indexName,
    isDisjunctiveFacetingEnabled = false,
    coroutineScope = coroutineScope,
    isAutoSendingHitsViewEvents = isAutoSendingHitsViewEvents,
)

fun TestSearcherForFacets(client: SearchClient, indexName: String, attribute: String) = FacetsSearcher(
    client = client,
    indexName = indexName,
    attribute = attribute,
    coroutineScope = TestCoroutineScope
)
