package com.algolia.instantsearch.helper.searcher.multi.internal

import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.helper.searcher.SearcherScope
import com.algolia.instantsearch.helper.searcher.internal.SearcherExceptionHandler
import com.algolia.instantsearch.helper.searcher.internal.withUserAgent
import com.algolia.instantsearch.helper.searcher.multi.MultiSearchComponent
import com.algolia.instantsearch.helper.searcher.multi.MultiSearcher
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
internal class MultiSearcherImpl(
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

    /**
     * Add a searcher component.
     *
     * @param component multi search component to add
     */
    override fun addSearcher(component: MultiSearchComponent<IndexedQuery, ResultSearch>): MultiSearchComponent<IndexedQuery, ResultSearch> {
        components += component
        return component
    }

    override fun setQuery(text: String?) {
        components.forEach { it.setQuery(text) }
    }

    override suspend fun search(): ResponseMultiSearch {
        val queries = components.map { it.indexedQuery }
        val request = MultiSearchService.Request(queries, strategy)
        return searchService.search(request, options)
    }

    override fun searchAsync(): Job {
        return coroutineScope.launch(exceptionHandler) {
            setLoading(true)
            val response = withContext(Dispatchers.Default) { search() }
            setResponse(response)
            setLoading(false)
        }.also {
            sequencer.addOperation(it)
        }
    }

    private fun setLoading(isLoading: Boolean) {
        this.isLoading.value = isLoading
        components.forEach {
            it.isLoading.value = isLoading
        }
    }

    private fun setResponse(response: ResponseMultiSearch) {
        this.response.value = response
        components.zip(response.results).forEach { (component, result) ->
            component.response.value = result.response
        }
    }

    override fun cancel() {
        sequencer.cancelAll()
    }

    fun collect(): Pair<List<IndexedQuery>, (List<SubscriptionValue<ResultSearch>>) -> Unit> {
        val (requests, completions) = components.map { it.collect() }.unzip()

        val rangePerCompletion = completions.zip(requests.flatRanges())

        val requestsList = requests.flatten()
        val completionsList: (List<MultiResult<Any>>) -> Unit = { result: List<MultiResult<Any>> ->
            rangePerCompletion.map { (completion, range) ->
                val resultForCompletion = result.map { it }
                completion(resultForCompletion)
            }
        }

        //return requestsList to completionsList
        return TODO()
    }

    /*   fun collect(): Pair<List<IndexedQuery>>, (List<MultiResult<Any>>) -> Unit> {
           val (requests, completions) = components.map { it.collect() }.unzip()
           val rangePerCompletion = completions.zip(requests.flatRanges())

           val requestsList = requests.flatten()
           val completionsList: (List<MultiResult<Any>>) -> Unit = { result: List<MultiResult<Any>> ->
               rangePerCompletion.map { (completion, range) ->
                   val resultForCompletion = result.map { it }
                   completion(resultForCompletion)
               }
           }

           return requestsList to completionsList
       }*/

    /**
     * Maps the nested lists to the ranges corresponding to the positions of the nested list elements in the flatten list
     * Example: [["a", "b", "c"], ["d", "e"], ["f", "g", "h"]] -> [0..<3, 3..<5, 5..<8]
     */
    private fun <T> List<List<T>>.flatRanges(): List<IntRange> {
        val ranges = mutableListOf<IntRange>()
        var offset = 0
        for (sublist in this) {
            val nextOffset = offset + sublist.size
            ranges += offset..sublist.size
            offset = nextOffset
        }
        return ranges
    }
}
