package com.algolia.instantsearch.relateditems.internal

import com.algolia.client.model.search.FacetFilters
import com.algolia.client.model.search.OptionalFilters
import com.algolia.client.model.search.SearchResponse
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.Presenter
import com.algolia.instantsearch.core.connection.AbstractConnection
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.extension.traceRelatedItems
import com.algolia.instantsearch.core.Indexable
import com.algolia.instantsearch.relateditems.MatchingPattern
import com.algolia.instantsearch.relateditems.internal.extensions.toFacetFilter
import com.algolia.instantsearch.relateditems.internal.extensions.toOptionalFilters
import com.algolia.instantsearch.searcher.SearcherForHits
import com.algolia.instantsearch.searcher.updateSearchParamsObject

internal data class RelatedItemsConnectionView<S, T>(
    private val searcher: S,
    private val view: HitsView<T>,
    private val hit: T,
    private val matchingPatterns: List<MatchingPattern<T>>,
    private val presenter: Presenter<SearchResponse, List<T>>,
) : AbstractConnection() where T : Indexable, S : SearcherForHits<*> {

    init {
        searcher.configureRelatedItems(hit, matchingPatterns)
        traceRelatedItems()
    }

    private val callback: Callback<SearchResponse?> = { response ->
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
        val facetFilters = hit.toFacetFilter(true).toFacetFilters()
        val optionalFilters = patterns.toOptionalFilters(hit)?.toOptionalFilters()
        updateSearchParamsObject { params ->
            params.copy(
                sumOrFiltersScores = true,
                facetFilters = facetFilters,
                optionalFilters = optionalFilters
            )
        }
    }

    private fun List<List<String>>.toFacetFilters(): FacetFilters {
        val groups = map { group -> FacetFilters.of(group.map { FacetFilters.of(it) }) }
        return FacetFilters.of(groups)
    }

    private fun List<List<String>>.toOptionalFilters(): OptionalFilters {
        val groups = map { group -> OptionalFilters.of(group.map { OptionalFilters.of(it) }) }
        return OptionalFilters.of(groups)
    }
}
