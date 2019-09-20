@file:JvmName("Searchers")
@file:JvmMultifileClass

package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.searcher.debounceFilteringInMillis
import com.algolia.instantsearch.helper.filter.state.FilterState
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads

/**
 * Connects this SearcherSingleIndex to a FilterState, updating the query on new filters
 * and updating the filterState when a response arrives.
 */
@JvmOverloads
public fun SearcherSingleIndex.connectFilterState(
    filterState: FilterState,
    debouncer: Debouncer = Debouncer(debounceFilteringInMillis)
): Connection {
    return SearcherSingleConnectionFilterState(this, filterState, debouncer)
}

/**
 * Connects this SearcherForFacets to a FilterState, updating the query on new filters
 * and updating the filterState when a response arrives.
 */
@JvmOverloads
public fun SearcherForFacets.connectFilterState(
    filterState: FilterState,
    debouncer: Debouncer = Debouncer(debounceFilteringInMillis)
): Connection {
    return SearcherForFacetsConnectionFilterState(this, filterState, debouncer)
}