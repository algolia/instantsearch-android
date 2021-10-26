package com.algolia.instantsearch.helper.searcher.multi.hits.internal

import com.algolia.instantsearch.helper.searcher.multi.internal.SearchService
import com.algolia.instantsearch.helper.searcher.multi.internal.extension.asResponseSearchList
import com.algolia.search.client.ClientSearch
import com.algolia.search.dsl.filters
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.FilterGroup
import com.algolia.search.model.filter.FilterGroupsConverter
import com.algolia.search.model.multipleindex.IndexQuery
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.Facet
import com.algolia.search.model.search.FacetStats
import com.algolia.search.transport.RequestOptions

/**
 * Search service for hits.
 */
internal class HitsSearchService(
    val client: ClientSearch
) : SearchService<HitsSearchService.Request, ResponseSearch> {

    override suspend fun search(request: Request, requestOptions: RequestOptions?): ResponseSearch {
        val (indexQuery, filterGroups, isDisjunctiveFacetingEnabled) = request
        return if (isDisjunctiveFacetingEnabled) {
            multiSearch(indexQuery, filterGroups, requestOptions)
        } else {
            indexSearch(indexQuery, requestOptions)
        }
    }

    /**
     * Converts an index query search into multiple queries, runs search, and aggregates all responses into one response.
     */
    private suspend fun multiSearch(
        indexQuery: IndexQuery,
        filterGroups: Set<FilterGroup<*>>,
        requestOptions: RequestOptions?
    ): ResponseSearch {
        val (queries, disjunctiveFacetCount) = advancedQueryOf(indexQuery, filterGroups)
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

    /**
     * Builds an [AdvancedQuery] based on [IndexQuery] and [FilterGroup]s.
     */
    internal fun advancedQueryOf(indexQuery: IndexQuery, filterGroups: Set<FilterGroup<*>>): AdvancedQuery {
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

    /**
     * Aggregate multiple [ResponseSearch]s into one [ResponseSearch].
     */
    internal fun aggregateResult(responses: List<ResponseSearch>, disjunctiveFacetCount: Int): ResponseSearch {
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

    /**
     * Hits service's request.
     */
    internal data class Request(
        val indexQuery: IndexQuery,
        val filterGroups: Set<FilterGroup<*>> = emptySet(),
        val isDisjunctiveFacetingEnabled: Boolean = true
    )

    /**
     * Advanced query composed of a list of queries.
     * (query for hits + queries for disjunctive facets + queries for hierarchical facets).
     */
    internal data class AdvancedQuery(
        val queries: List<IndexQuery>,
        val disjunctiveFacetCount: Int
    )
}
