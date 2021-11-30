package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.ExperimentalInstantSearch
import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.helper.extension.traceAnswersSearcher
import com.algolia.instantsearch.helper.searcher.internal.SearcherExceptionHandler
import com.algolia.instantsearch.helper.searcher.internal.withUserAgentTelemetry
import com.algolia.search.client.Index
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.AnswersQuery
import com.algolia.search.model.search.Language
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * The component handling Algolia Answers search requests and managing the search sessions.
 */
@ExperimentalInstantSearch
public class SearcherAnswers(
    public override var index: Index,
    public override val query: AnswersQuery = AnswersQuery("", listOf(Language.English)),
    public override val requestOptions: RequestOptions? = null,
    override val coroutineScope: CoroutineScope = SearcherScope(),
) : SearcherIndex<AnswersQuery> {

    override val isLoading: SubscriptionValue<Boolean> = SubscriptionValue(false)
    override val error: SubscriptionValue<Throwable?> = SubscriptionValue(null)
    override val response: SubscriptionValue<ResponseSearch?> = SubscriptionValue(null)

    private val sequencer = Sequencer()
    private val options get() = requestOptions.withUserAgentTelemetry()
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
            response.value = withContext(Dispatchers.Default) { search() }
            isLoading.value = false
        }.also {
            sequencer.addOperation(it)
        }
    }

    override suspend fun search(): ResponseSearch {
        return index.findAnswers(answersQuery = query, requestOptions = options)
    }

    override fun cancel() {
        sequencer.cancelAll()
    }
}
