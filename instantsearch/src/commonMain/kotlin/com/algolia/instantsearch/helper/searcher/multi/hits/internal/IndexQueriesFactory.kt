package com.algolia.instantsearch.helper.searcher.internal.service

import com.algolia.search.dsl.filters
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.FilterGroup
import com.algolia.search.model.filter.FilterGroupsConverter
import com.algolia.search.model.multipleindex.IndexQuery
import com.algolia.search.model.search.Query

internal interface IndexQueriesFactory {

    var filterGroups: Set<FilterGroup<*>>

    fun build(
        indexName: IndexName,
        query: Query = Query()
    ): List<IndexQuery>
}

internal fun IndexQueriesFactory(filterGroups: Set<FilterGroup<*>> = setOf()): IndexQueriesFactory {
    return IndexQueriesFactoryImpl(filterGroups)
}

private class IndexQueriesFactoryImpl(
    override var filterGroups: Set<FilterGroup<*>>
) : IndexQueriesFactory {

    var queryForResultsCount: Int = 0
    var queriesForDisjunctiveFacetsCount: Int = 0
    var queriesForHierarchicalFacetsCount = 0

    override fun build(
        indexName: IndexName,
        query: Query,
    ): List<IndexQuery> {
        val filtersAnd = filterGroups.filterIsInstance<FilterGroup.And<*>>().flatten()
        val filtersOr = filterGroups.filterIsInstance<FilterGroup.Or<*>>().flatten()
        val disjunctiveFacets = filtersOr.map { it.attribute }.toSet()
        val filtersOrFacet = filtersOr.filterIsInstance<Filter.Facet>()
        val filtersOrTag = filtersOr.filterIsInstance<Filter.Tag>()
        val filtersOrNumeric = filtersOr.filterIsInstance<Filter.Numeric>()
        val queryForResults = query.toIndexQuery(indexName).setFilters(filterGroups)
        val queriesForDisjunctiveFacets = disjunctiveFacets.map { attribute ->
            val groups = filterGroups.map { group ->
                if (group is FilterGroup.Or.Facet) {
                    FilterGroup.Or.Facet(group.filter { filter -> filter.attribute != attribute }.toSet())
                } else group
            }

            query
                .toIndexQuery(indexName)
                .setFacets(attribute)
                .optimize()
                .setFilters(groups.toSet())
        }
        val queriesForHierarchicalFacets = filterGroups.filterIsInstance<FilterGroup.And.Hierarchical>().flatMap {
            it.attributes
                .take(it.path.size + 1)
                .mapIndexed { index, attribute ->
                    query
                        .toIndexQuery(indexName)
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
        val queriesForResults = listOf(queryForResults)
        queryForResultsCount = queriesForResults.size
        queriesForDisjunctiveFacetsCount = queriesForDisjunctiveFacets.size
        queriesForHierarchicalFacetsCount = queriesForHierarchicalFacets.size
        return queriesForResults + queriesForDisjunctiveFacets + queriesForHierarchicalFacets
    }

    private fun Query.toIndexQuery(indexName: IndexName): IndexQuery {
        return IndexQuery(indexName, copy())
    }

    private fun IndexQuery.setFilters(groups: Set<FilterGroup<*>>): IndexQuery {
        query.filters = FilterGroupsConverter.SQL(groups)
        return this
    }

    private fun IndexQuery.setFacets(facet: Attribute?): IndexQuery {
        if (facet != null) query.facets = setOf(facet)
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

    private fun List<Filter>.combine(hierarchicalFilter: Filter.Facet?): List<Filter> {
        return hierarchicalFilter?.let { this + it } ?: this
    }
}
