package com.algolia.instantsearch.migration2to3

import com.algolia.client.model.search.AdvancedSyntaxFeatures
import com.algolia.client.model.search.AlternativesAsExact
import com.algolia.client.model.search.Distinct
import com.algolia.client.model.search.ExactOnSingleWordQuery
import com.algolia.client.model.search.QueryType
import com.algolia.client.model.search.RemoveStopWords


public interface BaseParameters {

    /**
     * Gives control over which attributes to retrieve and which not to retrieve.
     * Engine default: [*]
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/attributesToRetrieve/?language=kotlin]
     */
    public var attributesToRetrieve: List<Attribute>?

    /**
     * Maximum number of facet values to return for each facet during a regular search.
     * Engine default: 100
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/maxValuesPerFacet/?language=kotlin]
     */
    public var maxValuesPerFacet: Int?

    /**
     * Engine default: [SortFacetsBy.Count]
     * Controls how facet values are sorted.
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/sortFacetValuesBy/?language=kotlin]
     */
    public var sortFacetsBy: SortFacetsBy?

    /**
     * List of attributes to highlight.
     * Engine default: [*]
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/attributesToHighlight/?language=kotlin]
     */
    public var attributesToHighlight: List<Attribute>?

    /**
     * The HTML string to insert before the highlighted parts in all highlight and snippet results.
     * Needs to be used along [highlightPostTag].
     * Engine default: "<em>"
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/highlightPreTag/?language=kotlin]
     */
    public var highlightPreTag: String?

    /**
     * The HTML string to insert after the highlighted parts in all highlight and snippet results.
     * Needs to be used along [highlightPreTag].
     * Engine default: "</em>"
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/highlightPostTag/?language=kotlin]
     */
    public var highlightPostTag: String?

    /**
     * String used as an ellipsis indicator when a snippet is truncated.
     * Engine default: "…" (U+2026, HORIZONTAL ELLIPSIS)
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/snippetEllipsisText/?language=kotlin]
     */
    public var snippetEllipsisText: String?

    /**
     * Restrict highlighting and snippeting to items that matched the query.
     * Engine default: false
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/restrictHighlightAndSnippetArrays/?language=kotlin]
     */
    public var restrictHighlightAndSnippetArrays: Boolean?

    /**
     * Minimum number of characters a word in the query name must contain to accept matches with 1 typo.
     * Engine default: 4
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/minWordSizefor1Typo/?language=kotlin]
     */
    public var minWordSizeFor1Typo: Int?

    /**
     * Minimum number of characters a word in the query name must contain to accept matches with 2 typos.
     * Engine default: 8
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/minWordSizefor2Typos/?language=kotlin]
     */
    public var minWordSizeFor2Typos: Int?

    /**
     * Controls whether typo tolerance is enabled and how it is applied.
     * Engine defaults: true
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/typoTolerance/?language=kotlin]
     */
    public var typoTolerance: TypoTolerance?

    /**
     * Whether to allow typos on numbers (“numeric tokens”) in the query name.
     * Engine default: true
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/allowTyposOnNumericTokens/?language=kotlin]
     */
    public var allowTyposOnNumericTokens: Boolean?

    /**
     * List of attributes on which you want to disable typo tolerance.
     * Engine default: []
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/disabletypotoleranceonattributes/?language=kotlin]
     */
    public var disableTypoToleranceOnAttributes: List<Attribute>?

    /**
     * Treats singular, plurals, and other forms of declensions as matching terms.
     * Engine default: false
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/ignorePlurals/?language=kotlin]
     */
    public var ignorePlurals: IgnorePlurals?

    /**
     * Removes stop (task) words from the query before executing it.
     * Engine default: false
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/removeStopWords/?language=kotlin]
     */
    public var removeStopWords: RemoveStopWords?

    /**
     * Whether rules should be globally enabled.
     * Engine default: true
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/enableRules/?language=kotlin]
     */
    public var enableRules: Boolean?

    /**
     * Controls if and how query words are interpreted as [prefixes][https://www.algolia.com/doc/guides/textual-relevance/prefix-search/?language=kotlin].
     * Engine default: [QueryType.PrefixLast]
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/queryType/?language=kotlin]
     */
    public var queryType: QueryType?

    /**
     * Selects a strategy to remove words from the query when it doesn’t match any hits.
     * Engine default: [RemoveWordIfNoResults.None]
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/removeWordsIfNoResults/?language=kotlin]
     */
    public var removeWordsIfNoResults: RemoveWordIfNoResults?

    /**
     * Enables the advanced query syntax.
     * Engine default: false
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/advancedSyntax/?language=kotlin]
     */
    public var advancedSyntax: Boolean?

    /**
     * A list of words that should be considered as optional when found in the query.
     * Engine default: []
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/optionalWords/?language=kotlin]
     */
    public var optionalWords: List<String>?

    /**
     * List of [Attribute] on which you want to disable the exact ranking criterion.
     * Engine default: []
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/disableExactOnAttributes/?language=kotlin]
     */
    public var disableExactOnAttributes: List<Attribute>?

    /**
     * Controls how the exact ranking criterion is computed when the query contains only one word.
     * Engine default: [ExactOnSingleWordQuery.Attribute]
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/exactOnSingleWordQuery/?language=kotlin]
     */
    public var exactOnSingleWordQuery: ExactOnSingleWordQuery?

    /**
     * List of alternatives that should be considered an exact match by the exact ranking criterion.
     * Engine default: [[AlternativesAsExact.IgnorePlurals], [AlternativesAsExact.SingleWordSynonym]]
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/alternativesAsExact/?language=kotlin]
     */
    public var alternativesAsExact: List<AlternativesAsExact>?

    /**
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters//?language=kotlin]
     */
    public var advancedSyntaxFeatures: List<AdvancedSyntaxFeatures>?

    /**
     * Enables de-duplication or grouping of results.
     * Engine default: 0
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/distinct/?language=kotlin]
     */
    public var distinct: Distinct?

    /**
     * Whether to highlight and snippet the original word that matches the synonym or the synonym itself.
     * Engine default: true
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters//?language=kotlin]
     */
    public var replaceSynonymsInHighlight: Boolean?

    /**
     * Precision of the proximity ranking criterion.
     * Engine default: 1
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/minProximity/?language=kotlin]
     */
    public var minProximity: Int?

    /**
     * Choose which fields the response will contain. Applies to search and browse queries.
     * Engine default: [ResponseFields.All]
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/responseFields/?language=kotlin]
     */
    public var responseFields: List<ResponseFields>?

    /**
     * Maximum number of facet hits to return during a search for facet values.
     * Engine default: 10
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/maxFacetHits/?language=kotlin]
     */
    public var maxFacetHits: Int?
}
