package com.algolia.instantsearch.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
    @Nullable
    public final JSONObject content;
    /** Facets that will be treated as disjunctive (`OR`). By default, facets are conjunctive (`AND`). */
    public List<String> disjunctiveFacets;

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
    @Nullable
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
    public SearchResults(@Nullable JSONObject content) {
        //DISCUSS: what if mandatory missing? What about processingTimeMS? IF not mandatory, null or default value?
        this.content = content;
        if (content == null) {
            return;
        }
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
        aroundLatLng = Query.parseLatLng(content.optString("aroundLatLng", null));
        serverUsed = content.optString("serverUsed", null);
        parsedQuery = content.optString("parsedQuery", null);
        facets = parseFacets(content.optJSONObject("facets"));
        disjunctiveFacets = parseDisjunctiveFacets(content.optJSONObject("disjunctiveFacets"));
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

    @NonNull
    private List<String> parseDisjunctiveFacets(JSONObject disjunctiveFacets) {
        List<String> disjunctiveFacetList = new ArrayList<>();

        if (disjunctiveFacets != null) {
            final Iterator<String> iterator = disjunctiveFacets.keys();
            while (iterator.hasNext()) {
                disjunctiveFacetList.add(iterator.next());
            }
        }
        return disjunctiveFacetList;
    }

    @NonNull
    private Map<String, List<FacetValue>> parseFacets(@Nullable JSONObject facets) {
        Map<String, List<FacetValue>> facetMap = new HashMap<>();

        if (facets == null) {
            return facetMap;
        }

        final Iterator<String> attributesIterator = facets.keys();
        while (attributesIterator.hasNext()) {
            final String attribute = attributesIterator.next();
            final JSONObject attributeFacets = facets.optJSONObject(attribute);
            final Iterator<String> valuesIterator = attributeFacets.keys();
            List<FacetValue> facetList = new ArrayList<>();

            while (valuesIterator.hasNext()) {
                final String value = valuesIterator.next();
                final int count = attributeFacets.optInt(value);
                facetList.add(new FacetValue(value, count));
            }
            facetMap.put(attribute, facetList);
        }
        return facetMap;
    }
}