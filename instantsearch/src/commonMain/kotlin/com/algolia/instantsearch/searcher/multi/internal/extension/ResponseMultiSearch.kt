@file:OptIn(InternalSerializationApi::class)

package com.algolia.instantsearch.searcher.multi.internal.extension

import com.algolia.client.model.search.SearchResponse
import com.algolia.client.model.search.SearchResult
import com.algolia.instantsearch.migration2to3.ResponseMultiSearch
import com.algolia.instantsearch.migration2to3.ResultMultiSearch
import kotlinx.serialization.InternalSerializationApi

/**
 * Maps [ResultSearch] to a list of [ResultSearch].
 */
internal fun ResponseMultiSearch.asResultSearchList(): List<SearchResult> = results.map { it.response }

/**
 * Maps [ResultSearch] to a list of [ResponseSearch].
 */
internal fun ResponseMultiSearch.asResponseSearchList(): List<SearchResponse> =
    results.map { (it as ResultMultiSearch.Hits).response }
