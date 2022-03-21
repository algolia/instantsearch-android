package com.algolia.instantsearch.searcher

import com.algolia.instantsearch.ExperimentalInstantSearch
import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.extension.traceAnswersSearcher
import com.algolia.instantsearch.searcher.internal.SearcherExceptionHandler
import com.algolia.instantsearch.searcher.internal.withUserAgent
import com.algolia.search.ExperimentalAlgoliaClientAPI
import com.algolia.search.client.Index
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.AnswersQuery
import com.algolia.search.model.search.Language
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * The component handling Algolia Answers search requests and managing the search sessions.
 */
@ExperimentalInstantSearch
@OptIn(ExperimentalAlgoliaClientAPI::class)
public class SearcherAnswers(
    public var index: Index,
    public override val query: AnswersQuery = AnswersQuery("", listOf(Language.English)),
    public override val requestOptions: RequestOptions? = null,
    override val coroutineScope: CoroutineScope = SearcherScope(),
    override val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : SearcherForHits<AnswersQuery> {

    override val isLoading: SubscriptionValue<Boolean> = SubscriptionValue(false)
    override val error: SubscriptionValue<Throwable?> = SubscriptionValue(null)
    override val response: SubscriptionValue<ResponseSearch?> = SubscriptionValue(null)

    private val sequencer = Sequencer()
    private val options get() = requestOptions.withUserAgent()
    private val exceptionHandler = SearcherExceptionHandler(this)

    init {
        traceAnswersSearcher()
    }

    override fun setQuery(text: String?) {
        text?.let { query.query = it }
    }

    override fun searchAsync(): Job {
        return coroutineScope.launch(exceptionHandler) {
            isLoading.value = true
            response.value = search()
            isLoading.value = false
        }.also {
            sequencer.addOperation(it)
        }
    }

    override suspend fun search(): ResponseSearch = withContext(coroutineDispatcher) {
        index.findAnswers(answersQuery = query, requestOptions = options)
    }

    override fun cancel() {
        sequencer.cancelAll()
    }
}
