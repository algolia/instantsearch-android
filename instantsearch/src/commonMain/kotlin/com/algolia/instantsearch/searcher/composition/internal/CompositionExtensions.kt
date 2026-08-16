package com.algolia.instantsearch.searcher.composition.internal

import com.algolia.client.model.composition.Params
import com.algolia.client.model.composition.SearchResultsItem
import com.algolia.client.model.search.SearchParamsObject
import com.algolia.client.model.search.SearchResponse
import kotlinx.serialization.json.Json
import com.algolia.client.model.search.SearchForFacetValuesResponse
import com.algolia.client.model.composition.SearchForFacetValuesResults as CompositionSearchForFacetValuesResults
import com.algolia.client.model.search.FacetHits as SearchFacetHits

/**
 * Lenient json used to convert between structurally identical models generated for different API
 * client namespaces: the parameters or response fields not supported by the destination model are
 * dropped.
 */
private val compositionJson = Json {
    ignoreUnknownKeys = true
    encodeDefaults = false
}

/**
 * Converts search parameters to composition run parameters.
 * The parameters not supported by the Composition API are dropped.
 */
internal fun SearchParamsObject.toCompositionParams(): Params {
    val element = compositionJson.encodeToJsonElement(SearchParamsObject.serializer(), this)
    return compositionJson.decodeFromJsonElement(Params.serializer(), element)
}

/**
 * Annotates the requested facets with the `disjunctive` modifier for the given attributes.
 */
internal fun Params.annotateDisjunctiveFacets(disjunctiveAttributes: Set<String>): Params {
    val facets = facets
    if (disjunctiveAttributes.isEmpty() || facets.isNullOrEmpty()) return this
    return copy(
        facets = facets.map { attribute ->
            if (attribute in disjunctiveAttributes) "disjunctive($attribute)" else attribute
        }
    )
}

/**
 * Converts a composition run result to a regular [SearchResponse], allowing the reuse of all the
 * components consuming search responses.
 */
internal fun SearchResultsItem.toSearchResponse(): SearchResponse {
    val element = compositionJson.encodeToJsonElement(SearchResultsItem.serializer(), this)
    return compositionJson.decodeFromJsonElement(SearchResponse.serializer(), element)
}

/**
 * Converts a composition facet values search result to a regular [SearchForFacetValuesResponse].
 */
internal fun CompositionSearchForFacetValuesResults.toSearchForFacetValuesResponse(): SearchForFacetValuesResponse {
    return SearchForFacetValuesResponse(
        facetHits = facetHits.map { SearchFacetHits(value = it.value, highlighted = it.highlighted, count = it.count) },
        exhaustiveFacetsCount = exhaustiveFacetsCount,
        processingTimeMS = processingTimeMS,
    )
}
