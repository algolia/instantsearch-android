package com.algolia.instantsearch.helper.searcher.multi

import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.helper.searcher.SearcherScope
import com.algolia.instantsearch.helper.searcher.internal.SearcherExceptionHandler
import com.algolia.instantsearch.helper.searcher.internal.service.MultiSearchService
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
public class MultiSearcher(
    public val client: ClientSearch,
    public val strategy: MultipleQueriesStrategy = MultipleQueriesStrategy.None,
    public val requestOptions: RequestOptions? = null,
    override val coroutineScope: CoroutineScope = SearcherScope()
) : Searcher<ResponseMultiSearch> {

    override val isLoading: SubscriptionValue<Boolean> = SubscriptionValue(false)
    override val error: SubscriptionValue<Throwable?> = SubscriptionValue(null)
    override val response: SubscriptionValue<ResponseMultiSearch?> = SubscriptionValue(null)

    private val exceptionHandler = SearcherExceptionHandler(this)
    private val components: MutableList<MultiSearchComponent<IndexedQuery, ResultSearch>> = mutableListOf()
    private val sequencer = Sequencer()
    private val searchService = MultiSearchService(client)

    /**
     * Add a searcher component.
     *
     * @param component multi search component to add
     */
    public fun addSearcher(component: MultiSearchComponent<IndexedQuery, ResultSearch>): MultiSearchComponent<IndexedQuery, ResultSearch> {
        components += component
        return component
    }

    override fun setQuery(text: String?) {
        components.forEach { it.setQuery(text) }
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

    override suspend fun search(): ResponseMultiSearch {
        val queries = components.map { it.indexedQuery() }
        val request = MultiSearchService.Request(queries, strategy, requestOptions)
        return searchService.search(request)
    }


    override fun collect(): Pair<List<MultiRequest<Any>>, (List<MultiResult<Any>>) -> Unit> {
        val (requests, completions) = components.map { it.collect() }.unzip()
        val rangePerCompletion = completions.zip(flatRanges(requests))

        val requestsList = requests.flatten()
        val completionsList: (List<MultiResult<Any>>) -> Unit = { result: List<MultiResult<Any>> ->
            rangePerCompletion.map { (completion, range) ->
                val resultForCompletion = result.map { it }
                completion(resultForCompletion)
            }
        }

        return requestsList to completionsList
    }

    /// Maps the nested lists to the ranges corresponding to the positions of the nested list elements in the flatten list
    /// Example: [["a", "b", "c"], ["d", "e"], ["f", "g", "h"]] -> [0..<3, 3..<5, 5..<8]
    private fun <T> flatRanges(list: List<List<T>>): List<IntRange> {
        val ranges = mutableListOf<IntRange>()
        var offset = 0
        for (sublist in list) {
            val nextOffset = offset + sublist.size
            ranges += offset..sublist.size
            offset = nextOffset
        }
        return ranges
    }

    override fun cancel() {
        sequencer.cancelAll()
    }
}
