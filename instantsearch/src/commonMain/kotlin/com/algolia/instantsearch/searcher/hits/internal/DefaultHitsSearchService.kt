package com.algolia.instantsearch.searcher.hits.internal

import com.algolia.client.api.SearchClient
import com.algolia.client.model.search.FacetStats
import com.algolia.client.model.search.SearchResponse
import com.algolia.client.transport.RequestOptions
import com.algolia.instantsearch.filter.Facet
import com.algolia.instantsearch.filter.Filter
import com.algolia.instantsearch.filter.FilterGroup
import com.algolia.instantsearch.filter.FilterGroupsConverter
import com.algolia.instantsearch.searcher.multi.internal.types.IndexQuery
import com.algolia.instantsearch.searcher.multi.internal.types.ResultMultiSearch
import com.algolia.instantsearch.searcher.hits.internal.HitsSearchService.AdvancedQuery
import com.algolia.instantsearch.searcher.hits.internal.HitsSearchService.Request
import com.algolia.instantsearch.searcher.internal.search
import com.algolia.instantsearch.searcher.multi.internal.SearchService

/**
 * Search service for hits.
 */
internal interface HitsSearchService : SearchService<Request, SearchResponse> {

    /**
     * Client to perform search operations.
     */
    val client: SearchClient

    /**
     * Contains a [Set] of [FilterGroup], used for disjunctive and hierarchical faceting.
     */
    var filterGroups: Set<FilterGroup<*>>

    /**
     * Builds an [AdvancedQuery] based on [IndexQuery] and [FilterGroup]s.
     */
    fun advancedQueryOf(indexQuery: IndexQuery): AdvancedQuery

    /**
     * Aggregate multiple [SearchResponse]s into one [SearchResponse].
     */
    fun aggregateResult(responses: List<SearchResponse>, disjunctiveFacetCount: Int): SearchResponse

    /**
     * Hits service's request.
     */
    data class Request(
        val indexQuery: IndexQuery,
        val isDisjunctiveFacetingEnabled: Boolean
    )

    /**
     * Advanced query composed of a list of queries.
     * (query for hits + queries for disjunctive facets + queries for hierarchical facets).
     */
    data class AdvancedQuery(
        val queries: List<IndexQuery>,
        val disjunctiveFacetCount: Int
    )
}

/**
 * Default implementation of [HitsSearchService].
 */
internal class DefaultHitsSearchService(
    override val client: SearchClient,
    override var filterGroups: Set<FilterGroup<*>> = setOf()
) : HitsSearchService {

    override suspend fun search(request: Request, requestOptions: RequestOptions?): SearchResponse {
        val (indexQuery, isDisjunctiveFacetingEnabled) = request
        return if (isDisjunctiveFacetingEnabled) {
            multiSearch(indexQuery, requestOptions)
        } else {
            indexSearch(indexQuery, requestOptions)
        }
    }

    /**
     * Converts an index query search into multiple queries, runs search, and aggregates all responses into one response.
     */
    private suspend fun multiSearch(
        indexQuery: IndexQuery,
        requestOptions: RequestOptions?
    ): SearchResponse {
        val (queries, disjunctiveFacetCount) = advancedQueryOf(indexQuery)
        val response = client.search(requests = queries, requestOptions = requestOptions)
        val responses = response.results.mapNotNull { (it as? ResultMultiSearch.Hits)?.response }
        return aggregateResult(responses, disjunctiveFacetCount)
    }

    /**
     * Runs a search query to an index.
     */
    private suspend fun indexSearch(indexQuery: IndexQuery, requestOptions: RequestOptions? = null): SearchResponse {
        return client.searchSingleIndex(
            indexName = indexQuery.indexName,
            searchParams = indexQuery.query,
            requestOptions = requestOptions
        )
    }

    override fun advancedQueryOf(indexQuery: IndexQuery): AdvancedQuery {
        val filtersAnd = filterGroups.filterIsInstance<FilterGroup.And<*>>().flatten()
        val filtersOr = filterGroups.filterIsInstance<FilterGroup.Or<*>>().flatten()
        val disjunctiveFacets = filtersOr.map { it.attribute }.toSet()
        val filtersOrFacet = filtersOr.filterIsInstance<Filter.Facet>()
        val filtersOrTag = filtersOr.filterIsInstance<Filter.Tag>()
        val filtersOrNumeric = filtersOr.filterIsInstance<Filter.Numeric>()
        val queryForResults = indexQuery.clone().setFilters(filterGroups)
        val queriesForDisjunctiveFacets = disjunctiveFacets.map { attribute ->
            val groups = filterGroups.map { group ->
                if (group is FilterGroup.Or.Facet) {
                    FilterGroup.Or.Facet(group.filter { filter -> filter.attribute != attribute }.toSet())
                } else group
            }

            indexQuery.clone()
                .setFacets(attribute)
                .optimize()
                .setFilters(groups.toSet())
        }
        val queriesForHierarchicalFacets = filterGroups.filterIsInstance<FilterGroup.And.Hierarchical>().flatMap {
            it.attributes
                .take(it.path.size + 1)
                .mapIndexed { index, attribute ->
                    indexQuery.clone()
                        .filters(
                            filtersAnd.combine(it.path.getOrNull(index - 1)).minus(it.path.last()),
                            filtersOrFacet,
                            filtersOrTag,
                            filtersOrNumeric
                        )
                        .setFacets(attribute)
                        .optimize()
                }
        }
        val queries = listOf(queryForResults) + queriesForDisjunctiveFacets + queriesForHierarchicalFacets
        return AdvancedQuery(queries, disjunctiveFacets.size)
    }

    private fun IndexQuery.clone(): IndexQuery {
        return copy(query = query.copy())
    }

    private fun IndexQuery.setFilters(groups: Set<FilterGroup<*>>): IndexQuery {
        return copy(query = query.copy(filters = FilterGroupsConverter.SQL(groups)))
    }

    private fun IndexQuery.optimize(): IndexQuery {
        return copy(query = query.copy(
            attributesToRetrieve = listOf(),
            attributesToHighlight = listOf(),
            hitsPerPage = 0,
            analytics = false
        ))
    }

    private fun List<Filter>.combine(hierarchicalFilter: Filter.Facet?): List<Filter> {
        return hierarchicalFilter?.let { this + it } ?: this
    }

    private fun IndexQuery.filters(
        filtersAnd: List<Filter>,
        filtersOrFacet: List<Filter.Facet>,
        filtersOrTag: List<Filter.Tag>,
        filtersOrNumeric: List<Filter.Numeric>,
    ): IndexQuery {
        // Note: SearchParamsObject doesn't have a filters {} DSL in v3
        // We need to build the filters string manually
        val filterString = FilterGroupsConverter.SQL(
            setOf(
                FilterGroup.And.Facet(filtersAnd.filterIsInstance<Filter.Facet>().toSet()),
                FilterGroup.Or.Facet(filtersOrFacet.toSet()),
                FilterGroup.Or.Tag(filtersOrTag.toSet()),
                FilterGroup.Or.Numeric(filtersOrNumeric.toSet())
            )
        )
        return copy(query = query.copy(filters = filterString))
    }

    private fun IndexQuery.setFacets(facet: String?): IndexQuery {
        return copy(query = query.copy(facets = facet?.let { listOf(it) }))
    }

    override fun aggregateResult(responses: List<SearchResponse>, disjunctiveFacetCount: Int): SearchResponse {
        val resultsDisjunctiveFacets = responses.subList(1, 1 + disjunctiveFacetCount)
        val resultHierarchicalFacets = responses.subList(1 + disjunctiveFacetCount, responses.size)
        val facets = resultsDisjunctiveFacets.aggregateFacets()
        val facetStats = responses.aggregateFacetStats()
        val hierarchicalFacets = resultHierarchicalFacets.aggregateFacets()
        // Note: SearchResponse doesn't have copy() with these properties in v3
        // For now, return the first response with aggregated facets and stats
        // TODO: Properly merge SearchResponse properties
        return responses.first().copy(
            facetsStats = if (facetStats.isEmpty()) null else facetStats,
            facets = facets.mapValues { (_, facets) -> facets.associate { it.value to it.count } }
        )
    }

    private fun List<SearchResponse>.aggregateFacets(): Map<String, List<Facet>> {
        return fold(emptyMap<String, List<Facet>>()) { acc, result ->
            result.facets?.let { facets ->
                acc + facets.map { (attr, counts) ->
                    attr to counts.map { (value, count) -> Facet(value, count) }
                }
            } ?: acc
        }
    }

    private fun List<SearchResponse>.aggregateFacetStats(): Map<String, FacetStats> {
        return fold(emptyMap<String, FacetStats>()) { acc, result ->
            result.facetsStats?.let { acc + it } ?: acc
        }
    }
}
