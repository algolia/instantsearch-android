package com.algolia.instantsearch.helper.searcher.multi

import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.helper.searcher.FacetsSearcher
import com.algolia.instantsearch.helper.searcher.HitsSearcher
import com.algolia.instantsearch.helper.searcher.SearcherScope
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.multipleindex.MultipleQueriesStrategy
import com.algolia.search.model.search.Query
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

/**
 * Extracts queries from queries sources, performs search request and dispatches the results to the corresponding receivers.
 */
public class MultiSearcher(
    public val client: ClientSearch,
    public val strategy: MultipleQueriesStrategy = MultipleQueriesStrategy.None,
    public val requestOptions: RequestOptions? = null,
    override val coroutineScope: CoroutineScope = SearcherScope()
) : MultiSearchComponent<MultiRequest<Any>, MultiResult<Any>> {

    override val isLoading: SubscriptionValue<Boolean> = SubscriptionValue(false)
    override val error: SubscriptionValue<Throwable?> = SubscriptionValue(null)
    override val response: SubscriptionValue<Any?> = SubscriptionValue(null)

    private val components: MutableList<MultiSearchComponent<*, *>> = mutableListOf() // TODO: update types
    private val sequencer = Sequencer()

    /**
     * Add a searcher component.
     *
     * @param component multi search component to add
     */
    public fun addSearcher(component: MultiSearchComponent<*, *>): MultiSearchComponent<*, *> {
        components += component
        return component
    }

    override fun setQuery(text: String?) {
        components.forEach { it.setQuery(text) }
    }

    override fun searchAsync(): Job {
        TODO("Not yet implemented")
    }

    override suspend fun search(): Any {
        TODO("Not yet implemented")
    }

    override suspend fun collect(requests: List<MultiRequest<Any>>): List<MultiResult<Any>> {
        val requestsAndCompletions = components.map { it.collect() }
        val requests = requestsAndCompletions.map { it.first }
        val completions = requestsAndCompletions.map { it.second }

        /// Maps the nested lists to the ranges corresponding to the positions of the nested list elements in the flatten list
        /// Example: [["a", "b", "c"], ["d", "e"], ["f", "g", "h"]] -> [0..<3, 3..<5, 5..<8]
        fun <T> flatRanges(list: List<List<T>>): List<IntRange> {
            val ranges = mutableListOf<IntRange>()
            var offset = 0
            for (sublist in list) {
                val nextOffset = offset + sublist.size
                ranges += offset..sublist.size
                offset = nextOffset
            }
            return ranges
        }

        val rangePerCompletion = completions.zip(flatRanges(requests))

        for ((completion, range) in rangePerCompletion) {
            //val resultForCompletion = res
        }

        val flatMap = requests.flatMap { it }

        //   return (requests.flatMap { $0 }, { result in
        //      for (completion, range) in rangePerCompletion {
        //        let resultForCompletion = result.map { Array($0[range]) }
        //        completion(resultForCompletion)
        //      }
        //    })
    }

    override fun cancel() {
        sequencer.cancelAll()
    }

    override fun collect(): Pair<List<MultiRequest<Any>>, (Result<List<MultiResult<Any>>>) -> Unit> {
        TODO("Not yet implemented")
    }
}

/**
 * Adds a [HitsSearcher].
 */
fun MultiSearcher.addHitsSearcher(
    indexName: IndexName,
    query: Query = Query(),
    requestOptions: RequestOptions? = null
): HitsSearcher {
    val searcher = HitsSearcher()
    val addSearcher = addSearcher(searcher)
    return HitsSearcher().also {
        addSearcher(it)
    }
}

/**
 * Adds a [FacetsSearcher].
 */
fun MultiSearcher.addFacetsSearcher(
    indexName: IndexName,
    attribute: Attribute,
    query: Query = Query(),
    facetQuery: String = "",
    requestOptions: RequestOptions? = null
): FacetsSearcher {
    val searcher = FacetsSearcher()
    return addSearcher(searcher)
}
