package com.algolia.instantsearch.migration2to3



public interface CommonSearchParameters : BaseParameters {

    /**
     *  Overrides the query parameter and performs a more generic search that can be used to find "similar" results.
     *  Engine default: ""
     *  [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/similarQuery/?language=kotlin]
     */
    public var similarQuery: String?

    /**
     * Filter the query with numeric, facet and/or tag filters.
     * Engine default: ""
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/filters/?language=kotlin]
     */
    public var filters: String?

    /**
     * Filter hits by facet value.
     * Engine default: []
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/facetFilters/?language=kotlin]
     */
    public var facetFilters: List<List<String>>?

    /**
     * Create filters for ranking purposes, where records that match the filter are ranked highest.
     * Engine default: []
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/optionalFilters/?language=kotlin]
     */
    public var optionalFilters: List<List<String>>?

    /**
     * Filter on numeric attributes.
     * Engine default: []
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/numericFilters/?language=kotlin]
     */
    public var numericFilters: List<List<String>>?

    /**
     * Filter hits by tags.
     * Engine default: []
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/tagFilters/?language=kotlin]
     */
    public var tagFilters: List<List<String>>?

    /**
     * Determines how to calculate the total score for filtering.
     * Engine default: false
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/sumOrFiltersScores/?language=kotlin]
     */
    public var sumOrFiltersScores: Boolean?

    /**
     * Facets to retrieve.
     * Engine default: []
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/facets/?language=kotlin]
     */
    public var facets: Set<Attribute>?

    /**
     * Force faceting to be applied after de-duplication (via the Distinct setting).
     * Engine default: false
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/facetingAfterDistinct/?language=kotlin]
     */
    public var facetingAfterDistinct: Boolean?

    /**
     * Specify the page to retrieve.
     * Engine default: 0
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/page/?language=kotlin]
     */
    public var page: Int?

    /**
     * Specify the offset of the first hit to return.
     * Engine default: null
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/offset/?language=kotlin]
     */
    public var offset: Int?

    /**
     * Set the number of hits to retrieve (used only with offset).
     * Engine default: null
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/length/?language=kotlin]
     */
    public var length: Int?

    /**
     * Search for entries around a central geolocation, enabling a geo search within a circular area.
     * Engine default: null
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/aroundLatLng/?language=kotlin]
     */
    public var aroundLatLng: Point?

    /**
     * Whether to search entries around a given location automatically computed from the requester’s IP address.
     * Engine default: false
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/aroundLatLngViaIP/?language=kotlin]
     */
    public var aroundLatLngViaIP: Boolean?

    /**
     * Define the maximum radius for a geo search (in meters).
     * Engine default: null
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/aroundRadius/?language=kotlin]
     */
    public var aroundRadius: AroundRadius?

    /**
     * Precision of geo search (in meters), to add grouping by geo location to the ranking formula.
     * Engine default: 1
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/aroundPrecision/?language=kotlin]
     */
    public var aroundPrecision: AroundPrecision?

    /**
     * Minimum radius (in meters) used for a geo search when [aroundRadius] is not set.
     * Engine default: null
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/minimumAroundRadius/?language=kotlin]
     */
    public var minimumAroundRadius: Int?

    /**
     * Search inside a rectangular area (in geo coordinates).
     * Engine default: null
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/insideBoundingBox/?language=kotlin]
     */
    public var insideBoundingBox: List<BoundingBox>?

    /**
     * Search inside a polygon (in geo coordinates).
     * Engine default: null
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/insidePolygon/?language=kotlin]
     */
    public var insidePolygon: List<Polygon>?

    /**
     * Enables contextual rules.
     * Engine default: []
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/ruleContexts/?language=kotlin]
     */
    public var ruleContexts: List<String>?

    /**
     * Enable the Personalization feature.
     * Engine default: false
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/enablePersonalization/?language=kotlin]
     */
    public var enablePersonalization: Boolean?

    /**
     *  The `personalizationImpact` parameter sets the percentage of the impact that personalization has on ranking
     *  records.
     *  This is set at query time and therefore overrides any impact value you had set on your index.
     *  The higher the `personalizationImpact`, the more the results are personalized for the user, and the less the
     *  custom ranking is taken into account in ranking records.
     *
     *  Usage note:
     *
     *  - The value must be between 0 and 100 (inclusive).
     *  - This parameter isn't taken into account if `enablePersonalization` is `false`.
     *  - Setting `personalizationImpact` to `0` disables the Personalization feature, as if `enablePersonalization`
     *    were `false`.
     */
    public var personalizationImpact: Int?

    /**
     * Associates a certain user token with the current search.
     * Engine default: User ip address
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/userToken/?language=kotlin]
     */
    public var userToken: UserToken?

    /**
     * Retrieve detailed ranking information.
     * Engine default: false
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/getRankingInfo/?language=kotlin]
     */
    public var getRankingInfo: Boolean?

    /**
     * Enable the Click Analytics feature.
     * Engine default: false.
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/clickAnalytics/?language=kotlin]
     */
    public var clickAnalytics: Boolean?

    /**
     * Whether the current query will be taken into account in the Analytics.
     * Engine default: true
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/analytics/?language=kotlin]
     */
    public var analytics: Boolean?

    /**
     * List of tags to apply to the query in the analytics.
     * Engine default: []
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/analyticsTags/?language=kotlin]
     */
    public var analyticsTags: List<String>?

    /**
     * Whether to take into account an index’s synonyms for a particular search.
     * Engine default: true
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/synonyms/?language=kotlin]
     */
    public var synonyms: Boolean?

    /**
     * Whether to include or exclude a query from the processing-time percentile computation.
     * Engine default: true
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/percentileComputation/?language=kotlin]
     */
    public var percentileComputation: Boolean?

    /**
     * Whether this query should be taken into consideration by currently active ABTests.
     * Engine default: true
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/enableABTest/?language=kotlin]
     */
    public var enableABTest: Boolean?

    /**
     * Enriches the API’s response with meta-information as to how the query was processed.
     * It is possible to enable several [ExplainModule] independently.
     * Engine default: null
     * [Documentation][https://www.algolia.com/doc/api-reference/api-parameters/decompoundedAttributes/?language=kotlin]
     */
    public var explainModules: List<ExplainModule>?

    /**
     * List of supported languages with their associated language ISO code.
     * Provide an easy way to implement voice and natural languages best practices such as ignorePlurals,
     * removeStopWords, removeWordsIfNoResults, analyticsTags and ruleContexts.
     */
    public var naturalLanguages: List<Language>?
}
