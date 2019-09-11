package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.search.client.Index
import com.algolia.search.model.filter.FilterGroup
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.Query
import com.algolia.search.transport.RequestOptions
import kotlinx.coroutines.*
import kotlin.jvm.JvmField
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmSynthetic

/**
 * A specialized Searcher made to target a single index.
 */
public class SearcherSingleIndex @JvmOverloads constructor(
    /**
     * The [Index] to target.
     */
    @JvmField
    public var index: Index,
    /**
     * The [Query] used when searching.
     */
    @JvmField
    public val query: Query = Query(),
    /**
     * Eventual [RequestOptions] applied to the searches.
     */
    @JvmField
    public val requestOptions: RequestOptions? = null,
    /**
     * If true, disjunctive faceting will be used, triggering several requests when needed and aggregating their answers.
     *
     * @see <a href="https://www.algolia.com/doc/guides/managing-results/refine-results/faceting/#faceting-types-and-features"> Algolia's disjunctive faceting documentation</a>.
     */
    @JvmField
    public val isDisjunctiveFacetingEnabled: Boolean = true,
    override val coroutineScope: CoroutineScope = SearcherScope()
) : Searcher<ResponseSearch> {

    internal val sequencer = Sequencer()
        @JvmSynthetic get

    override val isLoading = SubscriptionValue(false)
    override val error = SubscriptionValue<Throwable?>(null)
    override val response = SubscriptionValue<ResponseSearch?>(null)

    private val options = requestOptions.withUserAgent()
    private val exceptionHandler = SearcherExceptionHandler(this)

    internal var filterGroups: Set<FilterGroup<*>> = setOf()
        @JvmSynthetic get

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
}