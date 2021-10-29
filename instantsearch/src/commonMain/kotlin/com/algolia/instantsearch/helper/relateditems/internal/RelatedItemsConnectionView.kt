package com.algolia.instantsearch.helper.relateditems.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.Presenter
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.helper.extension.traceRelatedItems
import com.algolia.instantsearch.helper.relateditems.MatchingPattern
import com.algolia.instantsearch.helper.relateditems.internal.extensions.toFacetFilter
import com.algolia.instantsearch.helper.relateditems.internal.extensions.toOptionalFilters
import com.algolia.instantsearch.helper.searcher.QueryHolder
import com.algolia.search.model.indexing.Indexable
import com.algolia.search.model.response.ResponseSearch

internal data class RelatedItemsConnectionView<T, S>(
    private val searcher: S,
    private val view: HitsView<T>,
    private val hit: T,
    private val matchingPatterns: List<MatchingPattern<T>>,
    private val presenter: Presenter<ResponseSearch, List<T>>,
) : ConnectionImpl() where T : Indexable, S : Searcher<ResponseSearch>, S : QueryHolder<*> {

    init {
        searcher.configureRelatedItems(hit, matchingPatterns)
        traceRelatedItems()
    }

    private val callback: Callback<ResponseSearch?> = { response ->
        if (response != null) {
            view.setHits(presenter(response))
        }
    }

    override fun connect() {
        super.connect()
        searcher.response.subscribe(callback)
    }

    override fun disconnect() {
        super.disconnect()
        searcher.response.unsubscribe(callback)
    }

    private fun <T> QueryHolder<*>.configureRelatedItems(
        hit: T,
        patterns: List<MatchingPattern<T>>,
    ) where T : Indexable {
        query.apply {
            sumOrFiltersScores = true
            facetFilters = hit.toFacetFilter(true)
            optionalFilters = patterns.toOptionalFilters(hit)
        }
    }
}
