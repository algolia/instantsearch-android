@file:Suppress("DEPRECATION")

package searcher

import com.algolia.instantsearch.ExperimentalInstantSearch
import com.algolia.instantsearch.searcher.SearcherAnswers
import com.algolia.instantsearch.searcher.SearcherScope
import com.algolia.instantsearch.searcher.facets.FacetsSearcher
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.client.api.InsightsClient
import com.algolia.client.api.SearchClient
import com.algolia.search.client.ClientInsights
import com.algolia.search.client.Index
import com.algolia.instantsearch.filter.Attribute
import com.algolia.search.model.IndexName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

val TestCoroutineScope = SearcherScope(Dispatchers.Default)

fun TestSearcherSingle(
    client: SearchClient,
    indexName: IndexName,
    insights: InsightsClient = ClientInsights(client.appId, client.apiKey),
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

fun TestSearcherForFacets(client: SearchClient, indexName: IndexName, attribute: String) = FacetsSearcher(
    client = client,
    indexName = indexName,
    attribute = attribute,
    coroutineScope = TestCoroutineScope
)

@OptIn(ExperimentalInstantSearch::class)
fun TestSearcherAnswers(index: String) = SearcherAnswers(
    index = index,
    coroutineScope = TestCoroutineScope
)
