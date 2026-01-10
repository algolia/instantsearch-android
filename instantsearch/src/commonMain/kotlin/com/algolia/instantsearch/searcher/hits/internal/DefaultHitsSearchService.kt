package com.algolia.instantsearch.searcher.hits.internal

import com.algolia.client.api.SearchClient
import com.algolia.client.model.search.FacetStats
import com.algolia.instantsearch.filter.state.filters
import com.algolia.instantsearch.migration2to3.Attribute
import com.algolia.instantsearch.migration2to3.Facet
import com.algolia.instantsearch.migration2to3.Filter
import com.algolia.instantsearch.migration2to3.FilterGroup
import com.algolia.instantsearch.migration2to3.IndexQuery
import com.algolia.instantsearch.migration2to3.RequestOptions
import com.algolia.instantsearch.migration2to3.ResponseSearch
import com.algolia.instantsearch.searcher.hits.internal.HitsSearchService.AdvancedQuery
import com.algolia.instantsearch.searcher.hits.internal.HitsSearchService.Request
import com.algolia.instantsearch.searcher.multi.internal.SearchService
import com.algolia.instantsearch.searcher.multi.internal.extension.asResponseSearchList
/**
 * Search service for hits.
 */
internal interface HitsSearchService : SearchService<Request, ResponseSearch> {

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
     * Aggregate multiple [ResponseSearch]s into one [ResponseSearch].
     */
    fun aggregateResult(responses: List<ResponseSearch>, disjunctiveFacetCount: Int): ResponseSearch

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

    override suspend fun search(request: Request, requestOptions: RequestOptions?): ResponseSearch {
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
    ): ResponseSearch {
        val (queries, disjunctiveFacetCount) = advancedQueryOf(indexQuery)
        val response = client.search(requests = queries, requestOptions = requestOptions)
        val responses = response.asResponseSearchList()
        return aggregateResult(responses, disjunctiveFacetCount)
    }

    /**
     * Runs a search query to an index.
     */
    private suspend fun indexSearch(indexQuery: IndexQuery, requestOptions: RequestOptions? = null): ResponseSearch {
        val index = client.initIndex(indexQuery.indexName)
        return index.search(indexQuery.query, requestOptions)
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
        query.filters = FilterGroupsConverter.SQL(groups)
        return this
    }

    private fun IndexQuery.optimize(): IndexQuery {
        query.apply {
            attributesToRetrieve = listOf()
            attributesToHighlight = listOf()
            hitsPerPage = 0
            analytics = false
        }
        return this
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
        query.apply {
            filters {
                and { +filtersAnd }
                orFacet { +filtersOrFacet }
                orTag { +filtersOrTag }
                orNumeric { +filtersOrNumeric }
            }
        }
        return this
    }

    private fun IndexQuery.setFacets(facet: Attribute?): IndexQuery {
        if (facet != null) query.facets = setOf(facet)
        return this
    }

    override fun aggregateResult(responses: List<ResponseSearch>, disjunctiveFacetCount: Int): ResponseSearch {
        val resultsDisjunctiveFacets = responses.subList(1, 1 + disjunctiveFacetCount)
        val resultHierarchicalFacets = responses.subList(1 + disjunctiveFacetCount, responses.size)
        val facets = resultsDisjunctiveFacets.aggregateFacets()
        val facetStats = responses.aggregateFacetStats()
        val hierarchicalFacets = resultHierarchicalFacets.aggregateFacets()
        return responses.first().copy(
            facetStatsOrNull = if (facetStats.isEmpty()) null else facetStats,
            disjunctiveFacetsOrNull = facets,
            hierarchicalFacetsOrNull = if (hierarchicalFacets.isEmpty()) null else hierarchicalFacets,
            exhaustiveFacetsCountOrNull = resultsDisjunctiveFacets.all { it.exhaustiveFacetsCountOrNull == true }
        )
    }

    private fun List<ResponseSearch>.aggregateFacets(): Map<Attribute, List<Facet>> {
        return fold(mapOf()) { acc, result ->
            result.facetsOrNull?.let { acc + it } ?: acc
        }
    }

    private fun List<ResponseSearch>.aggregateFacetStats(): Map<Attribute, FacetStats> {
        return fold(mapOf()) { acc, result ->
            result.facetStatsOrNull?.let { acc + it } ?: acc
        }
    }
}
