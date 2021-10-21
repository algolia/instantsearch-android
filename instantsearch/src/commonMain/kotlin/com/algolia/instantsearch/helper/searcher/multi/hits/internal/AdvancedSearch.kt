package com.algolia.instantsearch.helper.searcher.multi.hits.internal

import com.algolia.search.dsl.filters
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.FilterGroup
import com.algolia.search.model.filter.FilterGroupsConverter
import com.algolia.search.model.multipleindex.IndexQuery
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.Facet
import com.algolia.search.model.search.FacetStats

/**
 * Converts an index query search into multiple queries, runs search, and aggregates all responses into one response.
 */
internal suspend fun advancedSearch(
    indexQuery: IndexQuery,
    filterGroups: Set<FilterGroup<*>>,
    search: suspend (List<IndexQuery>) -> List<ResponseSearch>
): ResponseSearch {
    val advancedQuery = advancedQueryOf(indexQuery, filterGroups)
    val responses = search(advancedQuery.queries)
    return responses.aggregateResult(advancedQuery.disjunctiveFacetCount)
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
    val queryForResults = indexQuery.copy().setFilters(filterGroups)
    val queriesForDisjunctiveFacets = disjunctiveFacets.map { attribute ->
        val groups = filterGroups.map { group ->
            if (group is FilterGroup.Or.Facet) {
                FilterGroup.Or.Facet(group.filter { filter -> filter.attribute != attribute }.toSet())
            } else group
        }

        indexQuery.copy()
            .setFacets(attribute)
            .optimize()
            .setFilters(groups.toSet())
    }
    val queriesForHierarchicalFacets = filterGroups.filterIsInstance<FilterGroup.And.Hierarchical>().flatMap {
        it.attributes
            .take(it.path.size + 1)
            .mapIndexed { index, attribute ->
                indexQuery.copy()
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
private fun List<ResponseSearch>.aggregateResult(disjunctiveFacetCount: Int): ResponseSearch {
    val resultsDisjunctiveFacets = subList(1, 1 + disjunctiveFacetCount)
    val resultHierarchicalFacets = subList(1 + disjunctiveFacetCount, size)
    val facets = resultsDisjunctiveFacets.aggregateFacets()
    val facetStats = aggregateFacetStats()
    val hierarchicalFacets = resultHierarchicalFacets.aggregateFacets()
    return first().copy(
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
 * Advanced query composed of a list of queries.
 * (query for hits + queries for disjunctive facets + queries for hierarchical facets).
 */
internal class AdvancedQuery(
    val queries: List<IndexQuery>,
    val disjunctiveFacetCount: Int
)
