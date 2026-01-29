package com.algolia.instantsearch.searcher.internal

import com.algolia.client.api.SearchClient
import com.algolia.client.extensions.from
import com.algolia.client.model.search.SearchForFacetValuesResponse
import com.algolia.client.model.search.SearchForFacets
import com.algolia.client.model.search.SearchMethodParams
import com.algolia.client.model.search.SearchQuery
import com.algolia.client.model.search.SearchResponse
import com.algolia.client.model.search.SearchResponses
import com.algolia.client.model.search.SearchStrategy
import com.algolia.client.model.search.SearchTypeFacet
import com.algolia.client.model.search.SearchForHits
import com.algolia.client.transport.RequestOptions
import com.algolia.instantsearch.searcher.multi.internal.types.FacetIndexQuery
import com.algolia.instantsearch.searcher.multi.internal.types.IndexQuery
import com.algolia.instantsearch.searcher.multi.internal.types.IndexedQuery
import com.algolia.instantsearch.searcher.multi.internal.types.MultipleQueriesStrategy
import com.algolia.instantsearch.searcher.multi.internal.types.ResponseMultiSearch
import com.algolia.instantsearch.searcher.multi.internal.types.ResultMultiSearch

/**
 * Convert [IndexQuery] to [SearchQuery] (SearchForHits).
 */
internal fun IndexQuery.toSearchQuery(): SearchQuery {
    return SearchQuery.of(SearchForHits.from(query, indexName))
}

/**
 * Convert [FacetIndexQuery] to [SearchQuery] (SearchForFacets).
 */
internal fun FacetIndexQuery.toSearchQuery(): SearchQuery {
    // SearchForFacets needs all params from SearchParamsObject, but it's a data class so we need to copy them
    return SearchQuery.of(
        SearchForFacets(
            facet = facetAttribute,
            indexName = indexName,
            type = SearchTypeFacet.Facet,
            query = query.query,
            similarQuery = query.similarQuery,
            filters = query.filters,
            facetFilters = query.facetFilters,
            optionalFilters = query.optionalFilters,
            numericFilters = query.numericFilters,
            tagFilters = query.tagFilters,
            sumOrFiltersScores = query.sumOrFiltersScores,
            restrictSearchableAttributes = query.restrictSearchableAttributes,
            facets = query.facets?.toList(),
            facetingAfterDistinct = query.facetingAfterDistinct,
            page = query.page,
            offset = query.offset,
            length = query.length,
            aroundLatLng = query.aroundLatLng,
            aroundLatLngViaIP = query.aroundLatLngViaIP,
            aroundRadius = query.aroundRadius,
            aroundPrecision = query.aroundPrecision,
            minimumAroundRadius = query.minimumAroundRadius,
            insideBoundingBox = query.insideBoundingBox,
            insidePolygon = query.insidePolygon,
            naturalLanguages = query.naturalLanguages,
            ruleContexts = query.ruleContexts,
            personalizationImpact = query.personalizationImpact,
            userToken = query.userToken,
            getRankingInfo = query.getRankingInfo,
            synonyms = query.synonyms,
            clickAnalytics = query.clickAnalytics,
            analytics = query.analytics,
            analyticsTags = query.analyticsTags,
            percentileComputation = query.percentileComputation,
            enableABTest = query.enableABTest,
            attributesToRetrieve = query.attributesToRetrieve,
            ranking = query.ranking,
            relevancyStrictness = query.relevancyStrictness,
            attributesToHighlight = query.attributesToHighlight,
            attributesToSnippet = query.attributesToSnippet,
            highlightPreTag = query.highlightPreTag,
            highlightPostTag = query.highlightPostTag,
            snippetEllipsisText = query.snippetEllipsisText,
            restrictHighlightAndSnippetArrays = query.restrictHighlightAndSnippetArrays,
            hitsPerPage = query.hitsPerPage,
            minWordSizefor1Typo = query.minWordSizefor1Typo,
            minWordSizefor2Typos = query.minWordSizefor2Typos,
            typoTolerance = query.typoTolerance,
            allowTyposOnNumericTokens = query.allowTyposOnNumericTokens,
            disableTypoToleranceOnAttributes = query.disableTypoToleranceOnAttributes,
            ignorePlurals = query.ignorePlurals,
            removeStopWords = query.removeStopWords,
            queryLanguages = query.queryLanguages,
            decompoundQuery = query.decompoundQuery,
            enableRules = query.enableRules,
            enablePersonalization = query.enablePersonalization,
            queryType = query.queryType,
            removeWordsIfNoResults = query.removeWordsIfNoResults,
            mode = query.mode,
            semanticSearch = query.semanticSearch,
            advancedSyntax = query.advancedSyntax,
            optionalWords = query.optionalWords,
            disableExactOnAttributes = query.disableExactOnAttributes,
            exactOnSingleWordQuery = query.exactOnSingleWordQuery,
            alternativesAsExact = query.alternativesAsExact,
            advancedSyntaxFeatures = query.advancedSyntaxFeatures,
            distinct = query.distinct,
            replaceSynonymsInHighlight = query.replaceSynonymsInHighlight,
            minProximity = query.minProximity,
            responseFields = query.responseFields,
            maxValuesPerFacet = query.maxValuesPerFacet,
            sortFacetValuesBy = query.sortFacetValuesBy,
            attributeCriteriaComputedByMinProximity = query.attributeCriteriaComputedByMinProximity,
            renderingContent = query.renderingContent,
            enableReRanking = query.enableReRanking,
            reRankingApplyFilter = query.reRankingApplyFilter,
            facetQuery = facetQuery,
            maxFacetHits = null
        )
    )
}

/**
 * Convert [IndexedQuery] to [SearchQuery].
 */
internal fun IndexedQuery.toSearchQuery(): SearchQuery {
    return when (this) {
        is IndexQuery -> toSearchQuery()
        is FacetIndexQuery -> toSearchQuery()
    }
}

/**
 * Convert [MultipleQueriesStrategy] to [SearchStrategy].
 */
internal fun MultipleQueriesStrategy?.toSearchStrategy(): SearchStrategy? {
    return when (this) {
        null -> null
        is MultipleQueriesStrategy.None -> SearchStrategy.None
        is MultipleQueriesStrategy.StopIfEnoughMatches -> SearchStrategy.StopIfEnoughMatches
        is MultipleQueriesStrategy.Other -> SearchStrategy.StopIfEnoughMatches // fallback
    }
}

/**
 * Search with a list of [IndexedQuery] requests.
 * This extension method bridges migration2to3 types to v3 API.
 */
internal suspend fun SearchClient.search(
    requests: List<IndexedQuery>,
    strategy: MultipleQueriesStrategy? = null,
    requestOptions: RequestOptions? = null
): ResponseMultiSearch {
    val searchQueries = requests.map { it.toSearchQuery() }
    val searchStrategy = strategy.toSearchStrategy()
    val searchParams = SearchMethodParams(requests = searchQueries, strategy = searchStrategy)
    val response = this.search(searchMethodParams = searchParams, requestOptions = requestOptions)
    return response.toResponseMultiSearch()
}

/**
 * Convert [SearchResponses] to [ResponseMultiSearch].
 */
internal fun SearchResponses.toResponseMultiSearch(): ResponseMultiSearch {
    val results = this.results.map { result ->
        when (result) {
            is SearchResponse -> ResultMultiSearch.Hits(result)
            is SearchForFacetValuesResponse -> ResultMultiSearch.Facets(result)
            else -> throw IllegalArgumentException("Unknown result type: ${result::class}")
        }
    }
    return ResponseMultiSearch(results = results)
}
