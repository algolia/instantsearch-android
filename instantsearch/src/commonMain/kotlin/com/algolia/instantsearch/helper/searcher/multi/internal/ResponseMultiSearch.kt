package com.algolia.instantsearch.helper.searcher.multi.internal

import com.algolia.search.model.response.ResponseMultiSearch
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.response.ResultMultiSearch
import com.algolia.search.model.response.ResultSearch

/**
 * Maps [ResultSearch] to a list of [ResultSearch].
 */
internal fun ResponseMultiSearch.asResultSearchList(): List<ResultSearch> = results.map { it.response }

/**
 * Maps [ResultSearch] to a list of [ResponseSearch].
 */
internal fun ResponseMultiSearch.asResponseSearchList(): List<ResponseSearch> =
    results.map { (it as ResultMultiSearch.Hits).response }
