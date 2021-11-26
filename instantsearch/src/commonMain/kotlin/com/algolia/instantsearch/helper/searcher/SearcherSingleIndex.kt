package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.helper.extension.traceHitsSearcher
import com.algolia.instantsearch.helper.searcher.internal.SearcherExceptionHandler
import com.algolia.instantsearch.helper.searcher.internal.withUserAgent
import com.algolia.search.client.Index
import com.algolia.search.model.filter.FilterGroup
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.Query
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * The component handling search requests and managing the search sessions.
 * This implementation searches a single index.
 */
public class SearcherSingleIndex(
    public override var index: Index,
    public override val query: Query = Query(),
    public override val requestOptions: RequestOptions? = null,
    public val isDisjunctiveFacetingEnabled: Boolean = true,
    override val coroutineScope: CoroutineScope = SearcherScope(),
) : SearcherIndex<Query> {

    internal val sequencer = Sequencer()

    override val isLoading: SubscriptionValue<Boolean> = SubscriptionValue(false)
    override val error: SubscriptionValue<Throwable?> = SubscriptionValue(null)
    override val response: SubscriptionValue<ResponseSearch?> = SubscriptionValue(null)

    private val options = requestOptions.withUserAgent()
    private val exceptionHandler = SearcherExceptionHandler(this)

    internal var filterGroups: Set<FilterGroup<*>> = setOf()

    init {
        traceHitsSearcher(this)
    }

    override fun setQuery(text: String?) {
        this.query.query = text
    }

    override fun searchAsync(): Job {
        return coroutineScope.launch(exceptionHandler) {
            isLoading.value = true
            response.value = withContext(Dispatchers.Default) { search() }
            isLoading.value = false
        }.also {
            sequencer.addOperation(it)
        }
    }

    override suspend fun search(): ResponseSearch {
        return if (isDisjunctiveFacetingEnabled) {
            index.advancedSearch(query, filterGroups, options)
        } else {
            index.search(query, options)
        }
    }

    override fun cancel() {
        sequencer.cancelAll()
    }

    public companion object {

        /**
         * Creates [SearcherSingleIndex] instance.
         */
        public operator fun invoke(
            index: Index,
            query: Query = Query(),
            requestOptions: RequestOptions? = null,
            isDisjunctiveFacetingEnabled: Boolean = true,
            coroutineScope: CoroutineScope = SearcherScope(),
        ): SearcherSingleIndex = invoke(index, query, requestOptions, isDisjunctiveFacetingEnabled, coroutineScope)
    }
}
