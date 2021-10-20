package com.algolia.instantsearch.helper.searcher.multi.hits.internal

import com.algolia.search.dsl.filters
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.FilterGroup
import com.algolia.search.model.filter.FilterGroupsConverter
import com.algolia.search.model.multipleindex.IndexQuery
import com.algolia.search.model.multipleindex.IndexedQuery
import com.algolia.search.model.response.ResponseMultiSearch
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.response.ResultMultiSearch
import com.algolia.search.model.search.Facet
import com.algolia.search.model.search.FacetStats

/**
 * Converts an index query search into multiple queries, runs search, and aggregates all responses into one response.
 */
internal suspend fun advancedSearch(
    indexQuery: IndexQuery,
    filterGroups: Set<FilterGroup<*>>,
    search: suspend (List<IndexedQuery>) -> ResponseMultiSearch
): ResponseSearch {
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
    val response = search(queries)
    return response.aggregateResult(disjunctiveFacets.size)
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

private fun ResponseMultiSearch.aggregateResult(disjunctiveFacetCount: Int): ResponseSearch {
    val responses = results.map { (it as ResultMultiSearch.Hits).response }
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
