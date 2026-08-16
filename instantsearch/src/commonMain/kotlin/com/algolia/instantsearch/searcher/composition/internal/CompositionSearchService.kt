package com.algolia.instantsearch.searcher.composition.internal

import com.algolia.client.api.CompositionClient
import com.algolia.client.model.composition.RequestBody
import com.algolia.client.model.search.SearchParamsObject
import com.algolia.client.model.search.SearchResponse
import com.algolia.client.transport.RequestOptions
import com.algolia.instantsearch.filter.FilterGroup
import com.algolia.instantsearch.searcher.multi.internal.SearchService

/**
 * Search service running Algolia Compositions.
 */
internal interface CompositionSearchService : SearchService<CompositionSearchService.Request, SearchResponse> {

    /**
     * Client to perform composition run operations.
     */
    val client: CompositionClient

    /**
     * Contains a [Set] of [FilterGroup], used to determine the disjunctive facets attributes.
     */
    var filterGroups: Set<FilterGroup<*>>

    /**
     * Composition service's request.
     */
    data class Request(
        val compositionID: String,
        val query: SearchParamsObject,
    )
}

/**
 * Default implementation of [CompositionSearchService].
 *
 * The [SearchParamsObject] is converted to composition run parameters and the first result of the
 * composition run response is converted back to a regular [SearchResponse], allowing the reuse of
 * all the components consuming search responses.
 */
internal class DefaultCompositionSearchService(
    override val client: CompositionClient,
    override var filterGroups: Set<FilterGroup<*>> = setOf(),
) : CompositionSearchService {

    override suspend fun search(request: CompositionSearchService.Request, requestOptions: RequestOptions?): SearchResponse {
        val params = request.query
            .toCompositionParams()
            .annotateDisjunctiveFacets(disjunctiveFacetAttributes())
        val response = client.search(
            compositionID = request.compositionID,
            requestBody = RequestBody(params),
            requestOptions = requestOptions,
        )
        val result = response.results.firstOrNull()
            ?: error("The composition run response contains no results")
        return result.toSearchResponse()
    }

    /**
     * Attributes of the refined disjunctive (OR group) facets, annotated with the `disjunctive`
     * modifier in the requested facets, so that the Composition API computes their facet counts
     * disjunctively server-side.
     */
    private fun disjunctiveFacetAttributes(): Set<String> {
        return filterGroups
            .filterIsInstance<FilterGroup.Or.Facet>()
            .flatten()
            .map { it.attribute }
            .toSet()
    }
}
