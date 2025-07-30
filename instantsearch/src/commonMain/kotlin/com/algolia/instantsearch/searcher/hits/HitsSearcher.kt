package com.algolia.instantsearch.searcher.hits

import com.algolia.instantsearch.searcher.FilterGroupsHolder
import com.algolia.instantsearch.searcher.IndexNameHolder
import com.algolia.instantsearch.searcher.SearcherForHits
import com.algolia.instantsearch.searcher.SearcherScope
import com.algolia.instantsearch.searcher.hits.internal.DefaultHitsSearchService
import com.algolia.instantsearch.searcher.hits.internal.DefaultHitsSearcher
import com.algolia.instantsearch.searcher.internal.defaultDispatcher
import com.algolia.instantsearch.searcher.multi.MultiSearcher
import com.algolia.instantsearch.searcher.multi.internal.asMultiSearchComponent
import com.algolia.instantsearch.util.randomUuid
import com.algolia.search.client.ClientInsights
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.algolia.search.model.insights.UserToken
import com.algolia.search.model.search.Query
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

/**
 * The component handling search requests and managing the search sessions.
 * This implementation searches for hits.
 */
public interface HitsSearcher : SearcherForHits<Query>, IndexNameHolder, FilterGroupsHolder {

    /**
     * Flag defining if disjunctive faceting is enabled.
     */
    public val isDisjunctiveFacetingEnabled: Boolean

    /**
     * Flag defining whether the automatic hits view Insights events sending is enabled.
     *
     * When this flag is set to true (default), the HitsSearcher automatically send Insights events
     * with object IDs received from search results hits.
     */
    public var isAutoSendingHitsViewEvents: Boolean

    /**
     * User token assigned to automatically sent Insights events in the HitsSearcher component.
     *
     * This user token is used to identify unique users and associate their interactions with the
     * search results, which helps in generating accurate and personalized analytics data.
     *
     * Set this property to a specific user token to associate the automatically sent hits view
     * Insights events with a unique user. If not explicitly set during initialization, the HitsSearcher
     * generates and assigns a default user token.
     */
    public var userToken: UserToken
}

/**
 * Creates an instance of [HitsSearcher].
 *
 * @param client search client instance
 * @param insights insights client instance
 * @param indexName index name
 * @param query the query used for search
 * @param requestOptions request local configuration
 * @param coroutineScope scope of coroutine operations
 * @param coroutineDispatcher async search dispatcher
 * @param triggerSearchFor request condition
 * @param isAutoSendingHitsViewEvents flag defining whether the automatic hits view Insights events sending is enabled
 * @param userToken user token assigned to automatically sent Insights events in the HitsSearcher component
 */
public fun HitsSearcher(
    client: ClientSearch,
    indexName: IndexName,
    query: Query = Query(),
    insights: ClientInsights = ClientInsights(client.applicationID, client.apiKey),
    requestOptions: RequestOptions? = null,
    isDisjunctiveFacetingEnabled: Boolean = true,
    coroutineScope: CoroutineScope = SearcherScope(),
    coroutineDispatcher: CoroutineDispatcher = defaultDispatcher,
    triggerSearchFor: SearchForQuery = SearchForQuery.All,
    isAutoSendingHitsViewEvents: Boolean = false,
    userToken: UserToken = UserToken.anonymous(),
): HitsSearcher = DefaultHitsSearcher(
    searchService = DefaultHitsSearchService(client),
    insights = insights,
    indexName = indexName,
    query = query,
    requestOptions = requestOptions,
    isDisjunctiveFacetingEnabled = isDisjunctiveFacetingEnabled,
    coroutineScope = coroutineScope,
    coroutineDispatcher = coroutineDispatcher,
    triggerSearchFor = triggerSearchFor,
    isAutoSendingHitsViewEvents = isAutoSendingHitsViewEvents,
    userToken = userToken
)

/**
 * Creates an instance of [HitsSearcher].
 *
 * @param applicationID application ID
 * @param apiKey API Key
 * @param indexName index name
 * @param query the query used for search
 * @param requestOptions request local configuration
 * @param coroutineScope scope of coroutine operations
 * @param coroutineDispatcher async search dispatcher
 * @param triggerSearchFor request condition
 * @param isAutoSendingHitsViewEvents flag defining whether the automatic hits view Insights events sending is enabled
 * @param userToken user token assigned to automatically sent Insights events in the HitsSearcher component
 */
public fun HitsSearcher(
    applicationID: ApplicationID,
    apiKey: APIKey,
    indexName: IndexName,
    query: Query = Query(),
    requestOptions: RequestOptions? = null,
    isDisjunctiveFacetingEnabled: Boolean = true,
    coroutineScope: CoroutineScope = SearcherScope(),
    coroutineDispatcher: CoroutineDispatcher = defaultDispatcher,
    triggerSearchFor: SearchForQuery = SearchForQuery.All,
    isAutoSendingHitsViewEvents: Boolean = false,
    userToken: UserToken = UserToken.anonymous(),
): HitsSearcher = HitsSearcher(
    client = ClientSearch(applicationID, apiKey),
    insights = ClientInsights(applicationID, apiKey),
    indexName = indexName,
    query = query,
    requestOptions = requestOptions,
    isDisjunctiveFacetingEnabled = isDisjunctiveFacetingEnabled,
    coroutineScope = coroutineScope,
    coroutineDispatcher = coroutineDispatcher,
    triggerSearchFor = triggerSearchFor,
    isAutoSendingHitsViewEvents = isAutoSendingHitsViewEvents,
    userToken = userToken,
)

/**
 * Adds a [HitsSearcher] to the [MultiSearcher] instance.
 *
 * @param indexName index name
 * @param query the query used for search
 * @param requestOptions request local configuration
 * @param triggerSearchFor request condition
 * @param isAutoSendingHitsViewEvents flag defining whether the automatic hits view Insights events sending is enabled
 * @param userToken user token assigned to automatically sent Insights events in the HitsSearcher component
 */
public fun MultiSearcher.addHitsSearcher(
    indexName: IndexName,
    query: Query = Query(),
    requestOptions: RequestOptions? = null,
    isDisjunctiveFacetingEnabled: Boolean = true,
    triggerSearchFor: SearchForQuery = SearchForQuery.All,
    isAutoSendingHitsViewEvents: Boolean = false,
    userToken: UserToken = UserToken.anonymous(),
): HitsSearcher {
    return DefaultHitsSearcher(
        searchService = DefaultHitsSearchService(client),
        insights = ClientInsights(applicationID = client.applicationID, apiKey = client.apiKey),
        indexName = indexName,
        query = query,
        requestOptions = requestOptions,
        isDisjunctiveFacetingEnabled = isDisjunctiveFacetingEnabled,
        coroutineScope = coroutineScope,
        coroutineDispatcher = coroutineDispatcher,
        triggerSearchFor = triggerSearchFor,
        isAutoSendingHitsViewEvents = isAutoSendingHitsViewEvents,
        userToken = userToken,
    ).also { addSearcher(it.asMultiSearchComponent()) }
}

private fun UserToken.Companion.anonymous(): UserToken {
    return UserToken("anonymous-${randomUuid()}")
}
