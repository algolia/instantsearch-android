package com.algolia.instantsearch.helper.relateditems.internal.extensions

import com.algolia.instantsearch.helper.relateditems.MatchingPattern
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.indexing.Indexable

internal fun <T> SearcherSingleIndex.configureRelatedItems(
    hit: T,
    patterns: List<MatchingPattern<T>>
) where T : Indexable {
    query.apply {
        sumOrFiltersScores = true
        facetFilters = hit.toFacetFilter(true)
        optionalFilters = patterns.toOptionalFilters(hit)
    }
}
