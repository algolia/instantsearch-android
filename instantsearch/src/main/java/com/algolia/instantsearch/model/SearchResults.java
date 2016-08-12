package com.algolia.instantsearch.model;

import com.algolia.search.saas.Query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SearchResults {
    /** The received JSON content. */
    public JSONObject content;
    /** Facets that will be treated as disjunctive (`OR`). By default, facets are conjunctive (`AND`). */
    public List<String> disjunctiveFacets; //TODO

    // Mandatory attributes
    /** Facets for the last results. */
    public Map<String, List<FacetValue>> facets;
    /** Hits. */
    public JSONArray hits;
    /** Total number of hits. */
    public int nbHits;
    /** Query text that produced those results. Should be identical to `params.query`. */
    public String query;
    /** Query parameters that produced those results. */
    public String params;
    /** Processing time of the last query (in ms). */
    public int processingTimeMS;
    /** Last returned page. */
    public Integer page;
    /** Total number of pages. */
    public Integer nbPages;
    /** Number of hits per page. */
    public Integer hitsPerPage;

    // Optional attributes
    /** Whether facet counts are exhaustive. */
    public Boolean exhaustiveFacetsCount;

    /** Used to return warnings about the query. Should be null most of the time. */
    public String message;

    /**
     * A markup text indicating which parts of the original query have been removed
     * in order to retrieve a non-empty result set.
     * The removed parts are surrounded by `<em>` tags.
     *
     * Note: Only returned when `removeWordsIfNoResults` is set.
     */
    public String queryAfterRemoval;

    /**
     * The computed geo location.
     *
     * Note: Only returned when `aroundLatLngViaIP` is set.
     */
    public Query.LatLng aroundLatLng;

    /**
     * The automatically computed radius.
     *
     * Note: Only returned for geo queries without an explicitly specified radius (see `aroundRadius`).
     */
    public Integer automaticRadius;


    /**
     * Actual host name of the server that processed the request. (Our DNS supports automatic failover
     * and load balancing, so this may differ from the host name used in the request.)
     *
     * Note: Only returned when `getRankingInfo` is true.
     */
    public String serverUsed;

    /**
     * The query string that will be searched, after normalization.
     *
     * Note: Only returned when `getRankingInfo` is true.
     */
    public String parsedQuery;

    /**
     * Whether a timeout was hit when computing the facet counts.
     * When true, the counts will be interpolated (i.e. approximate). See also `exhaustiveFacetsCount`.
     *
     * Note: Only returned when `getRankingInfo` is true.
     */
    public Boolean timeoutCounts;

    /**
     * Whether a timeout was hit when retrieving the hits. When true, some results may be missing.
     *
     * Note: Only returned when `getRankingInfo` is true.
     */
    public Boolean timeoutHits;

    /**
     * Build a SearchResult object from a raw JSON response.
     *
     * @param content the JSON content to parse.
     * @throws IllegalStateException if the server response misses any mandatory field.
     */
    public SearchResults(JSONObject content) {
        //TODO: Discuss: what if mandatory missing? What about processingTimeMS? IF not mandatory, null or default value?
        this.content = content;
        // Mandatory attributes, throw if any is missing
        try {
            hits = content.getJSONArray("hits");
            nbHits = content.getInt("nbHits");
            query = content.getString("query");
            params = content.getString("params");
            processingTimeMS = content.getInt("processingTimeMS");
        } catch (JSONException e) {
            throw new IllegalStateException("Invalid response from server.", e);
        }
        // Optional attributes, ignore and set to null if missing
        message = content.optString("message", null);
        queryAfterRemoval = content.optString("queryAfterRemoval", null);
        aroundLatLng = parseLatLng(content.optString("aroundLatLng", null));
        serverUsed = content.optString("serverUsed", null);
        parsedQuery = content.optString("parsedQuery", null);
        try {
            facets = parseFacets(content.optJSONObject("facets")); //TODO: Refact with has?
        } catch (JSONException ignored) {
        }
        page = content.optInt("page");
        nbPages = content.optInt("nbPages");
        hitsPerPage = content.optInt("hitsPerPage");
        exhaustiveFacetsCount = content.optBoolean("exhaustiveFacetsCount");
        try {
            automaticRadius = content.getInt("automaticRadius");
        } catch (JSONException ignored) {
        }
        try {
            timeoutCounts = content.getBoolean("timeoutCounts");
            timeoutHits = content.getBoolean("timeoutHits");
        } catch (JSONException ignored) {
        }
    }

    private Query.LatLng parseLatLng(String value) { //TODO: Merge with Query.getLatLng()
        if (value == null) {
            return null;
        }
        String[] components = value.split(",");
        if (components.length != 2) {
            return null;
        }
        try {
            return new Query.LatLng(Double.valueOf(components[0]), Double.valueOf(components[1]));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Map<String, List<FacetValue>> parseFacets(JSONObject facets) throws JSONException {
        Map<String, List<FacetValue>> facetMap = new HashMap<>();

        if (facets == null) {
            return facetMap;
        }

        final Iterator<String> attributesIterator = facets.keys();
        while (attributesIterator.hasNext()) {
            final String attribute = attributesIterator.next();
            final JSONObject attributeFacets = facets.getJSONObject(attribute);
            final Iterator<String> valuesIterator = attributeFacets.keys();
            List<FacetValue> facetList = new ArrayList<>();

            while (valuesIterator.hasNext()) {
                final String value = valuesIterator.next();
                final int count = attributeFacets.getInt(value);
                facetList.add(new FacetValue(value, count));
            }
            facetMap.put(attribute, facetList);
        }
        return facetMap;
    }
}