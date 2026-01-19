package com.algolia.instantsearch.searcher

import com.algolia.client.model.search.SearchParamsObject
import com.algolia.client.model.search.SearchResponse
import com.algolia.client.model.search.SupportedLanguage
import com.algolia.client.transport.RequestOptions
import com.algolia.instantsearch.ExperimentalInstantSearch
import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.extension.traceAnswersSearcher
import com.algolia.instantsearch.searcher.internal.SearcherExceptionHandler
import com.algolia.instantsearch.searcher.internal.defaultDispatcher
import com.algolia.instantsearch.searcher.internal.runAsLoading
import com.algolia.instantsearch.searcher.internal.withAlgoliaAgent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * The component handling Algolia Answers search requests and managing the search sessions.
 */
@Deprecated("Answers feature is deprecated")
@ExperimentalInstantSearch
@OptIn(ExperimentalAlgoliaClientAPI::class)
public class SearcherAnswers(
    public var index: String,
    public override val query: SearchParamsObject = SearchParamsObject("", naturalLanguages =listOf(SupportedLanguage.En)),
    public override val requestOptions: RequestOptions? = null,
    override val coroutineScope: CoroutineScope = SearcherScope(),
    override val coroutineDispatcher: CoroutineDispatcher = defaultDispatcher,
) : SearcherForHits<SearchParamsObject> {

    override val isLoading: SubscriptionValue<Boolean> = SubscriptionValue(false)
    override val error: SubscriptionValue<Throwable?> = SubscriptionValue(null)
    override val response: SubscriptionValue<SearchResponse?> = SubscriptionValue(null)

    private val sequencer = Sequencer()
    private val options get() = requestOptions.withAlgoliaAgent()
    private val exceptionHandler = SearcherExceptionHandler(this)

    init {
        traceAnswersSearcher()
    }

    override fun setQuery(text: String?) {
        text?.let { query.query = it }
    }

    override fun searchAsync(): Job {
        return coroutineScope.launch(exceptionHandler) {
            isLoading.runAsLoading {
                response.value = search()
            }
        }.also {
            sequencer.addOperation(it)
        }
    }

    override suspend fun search(): SearchResponse = withContext(coroutineDispatcher) {
        index.findAnswers(answersQuery = query, requestOptions = options)
    }

    override fun cancel() {
        sequencer.cancelAll()
    }
}
