package com.algolia.instantsearch.relateditems.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.Presenter
import com.algolia.instantsearch.core.connection.AbstractConnection
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.extension.traceRelatedItems
import com.algolia.instantsearch.relateditems.MatchingPattern
import com.algolia.instantsearch.relateditems.internal.extensions.toFacetFilter
import com.algolia.instantsearch.relateditems.internal.extensions.toOptionalFilters
import com.algolia.instantsearch.searcher.SearcherForHits
import com.algolia.search.model.indexing.Indexable
import com.algolia.search.model.response.ResponseSearch

internal data class RelatedItemsConnectionView<S, T>(
    private val searcher: S,
    private val view: HitsView<T>,
    private val hit: T,
    private val matchingPatterns: List<MatchingPattern<T>>,
    private val presenter: Presenter<ResponseSearch, List<T>>,
) : AbstractConnection() where T : Indexable, S : SearcherForHits<*> {

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

    private fun <T> SearcherForHits<*>.configureRelatedItems(
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
