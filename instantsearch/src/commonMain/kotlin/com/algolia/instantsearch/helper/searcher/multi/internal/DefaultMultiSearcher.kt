package com.algolia.instantsearch.helper.searcher.multi.internal

import com.algolia.instantsearch.ExperimentalInstantSearch
import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.helper.extension.traceMultiSearcher
import com.algolia.instantsearch.helper.searcher.SearcherScope
import com.algolia.instantsearch.helper.searcher.internal.SearcherExceptionHandler
import com.algolia.instantsearch.helper.searcher.internal.withUserAgent
import com.algolia.instantsearch.helper.searcher.multi.MultiSearcher
import com.algolia.instantsearch.helper.searcher.multi.internal.extension.asResultSearchList
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.multipleindex.IndexedQuery
import com.algolia.search.model.multipleindex.MultipleQueriesStrategy
import com.algolia.search.model.response.ResponseMultiSearch
import com.algolia.search.model.response.ResultSearch
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Extracts queries from queries sources, performs search request and dispatches the results to the corresponding receivers.
 */
@ExperimentalInstantSearch
internal class DefaultMultiSearcher(
    override val client: ClientSearch,
    val strategy: MultipleQueriesStrategy = MultipleQueriesStrategy.None,
    val requestOptions: RequestOptions? = null,
    override val coroutineScope: CoroutineScope = SearcherScope()
) : MultiSearcher() {

    override val isLoading: SubscriptionValue<Boolean> = SubscriptionValue(false)
    override val error: SubscriptionValue<Throwable?> = SubscriptionValue(null)
    override val response: SubscriptionValue<ResponseMultiSearch?> = SubscriptionValue(null)

    private val exceptionHandler = SearcherExceptionHandler(this)
    private val components: MutableList<MultiSearchComponent<IndexedQuery, ResultSearch>> = mutableListOf()
    private val sequencer = Sequencer()
    private val searchService = MultiSearchService(client)
    private val options = requestOptions.withUserAgent()

    init {
        isLoading.subscribe { loading ->
            components.forEach { it.isLoading.value = loading }
        }

        error.subscribe { throwable ->
            components.forEach { it.error.value = throwable }
            isLoading.value = false
        }
    }

    init {
        traceMultiSearcher()
    }

    /**
     * Add a searcher component.
     *
     * @param component multi search component to add
     */
    override fun addSearcher(component: MultiSearchComponent<IndexedQuery, ResultSearch>) {
        components += component
    }

    override fun setQuery(text: String?) {
        components.forEach { it.setQuery(text) }
    }

    override suspend fun search(): ResponseMultiSearch {
        val queries = components.flatMap { it.collect().first }
        val request = MultiSearchService.Request(queries, strategy)
        return searchService.search(request, options)
    }

    override fun searchAsync(): Job {
        return coroutineScope.launch(exceptionHandler) {
            isLoading.value = true
            val (queries, completion) = collect()
            val response = withContext(Dispatchers.Default) {
                val request = MultiSearchService.Request(queries, strategy)
                searchService.search(request, options)
            }
            onSearchResponse(response, completion)
            isLoading.value = false
        }.also {
            sequencer.addOperation(it)
        }
    }

    /**
     * Sets response search and dispatches the results to the multi-search components.
     */
    private fun onSearchResponse(response: ResponseMultiSearch, completion: (List<ResultSearch>) -> Unit) {
        this.response.value = response
        completion(response.asResultSearchList())
    }

    override fun cancel() {
        sequencer.cancelAll()
    }

    /**
     * Collects lists of requests and callbacks from all its search components.
     */
    private fun collect(): Pair<List<IndexedQuery>, (List<ResultSearch>) -> Unit> {
        val (requests, completions: List<(List<ResultSearch>) -> Unit>) = components.map { it.collect() }.unzip()
        val rangePerCompletion = completions.zip(requests.flatRanges())
        val requestsList = requests.flatten()
        val completionsList: (List<ResultSearch>) -> Unit = { results ->
            rangePerCompletion.map { (completion, range) ->
                val resultForCompletion = results.slice(range)
                completion(resultForCompletion)
            }
        }
        return requestsList to completionsList
    }

    /**
     * Maps the nested lists to the ranges corresponding to the positions of the nested list elements in the flatten list
     * Example: [["a", "b", "c"], ["d", "e"], ["f", "g", "h"]] -> [0..2, 3..4, 5..7]
     */
    private fun <T> List<List<T>>.flatRanges(): List<IntRange> {
        val ranges = mutableListOf<IntRange>()
        var offset = 0
        for (sublist in this) {
            val nextOffset = offset + sublist.size
            ranges += offset until nextOffset
            offset = nextOffset
        }
        return ranges
    }
}
