package com.algolia.instantsearch.helper.searcher.multi.hits.internal

import com.algolia.instantsearch.helper.searcher.multi.internal.SearchService
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.multipleindex.IndexQuery
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.transport.RequestOptions

internal class HitsService(
    val client: ClientSearch
) : SearchService<IndexQuery, ResponseSearch> {

    override suspend fun search(request: IndexQuery, requestOptions: RequestOptions?): ResponseSearch {
        // TODO: disjunctive faceting
        val index = client.initIndex(request.indexName)
        return index.search(request.query, requestOptions)
    }

    // Other approach:
    // val queries = listOf(request)
    // val response = client.search(requests = queries, requestOptions = requestOptions)
    // return response.results.first().asResponseSearch()
    //
    // private fun ResultMultiSearch<ResultSearch>.asResponseSearch() = (this as ResultMultiSearch.Hits).response
}
