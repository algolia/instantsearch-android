package com.algolia.instantsearch.core.model;

import com.algolia.search.saas.Query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Describes the results corresponding to a search request.
 */
@SuppressWarnings("WeakerAccess")
public class SearchResults {
    /** The received JSON content. */
    @NonNull
    public final JSONObject content;
    /** The facets that will be treated as disjunctive ({@code OR}). By default, facets are conjunctive ({@code AND}). */
    public Map<String, List<FacetValue>> disjunctiveFacets;

    // Mandatory attributes
    /** The facets for the last results. */
    public Map<String, List<FacetValue>> facets;
    /** The response's hits. */
    @NonNull
    public JSONArray hits;
    /** The total number of hits. */
    public int nbHits;
    /** The query text that produced those results. Should be identical to {@link SearchResults#params params.query}}. */
    @NonNull
    public String query;
    /** The query parameters that produced those results. */
    @NonNull
    public String params;
    /** The processing time of the last query (in ms). */
    @NonNull
    public int processingTimeMS;
    /** The last returned page. */
    @NonNull
    public Integer page;
    /** The total number of pages. */
    @NonNull
    public Integer nbPages;
    /** The number of hits per page. */
    @NonNull
    public Integer hitsPerPage;

    // Optional attributes
    /** Whether facet counts are exhaustive. */
    @NonNull
    public Boolean exhaustiveFacetsCount;

    /** The eventual warnings about the query. Should be null most of the time. */
    @Nullable
    public String message;

    /**
     * The markup text indicating which parts of the original query have been removed
     * in order to retrieve a non-empty result set.
     * The removed parts are surrounded by {@code <em></em>} tags.
     * <p>
     * Note: Only returned when {@link Query#getRemoveWordsIfNoResults() removeWordsIfNoResults} is {@code true}.
     */
    @Nullable
    public String queryAfterRemoval;

    /**
     * The computed geo location.
     * <p>
     * Note: Only returned when {@link Query#getAroundLatLngViaIP() aroundLatLngViaIp} is {@code true}.
     */
    @Nullable
    public Query.LatLng aroundLatLng;

    /**
     * The automatically computed radius.
     * <p>
     * Note: Only returned for geo queries without an explicitly specified radius (see {@link Query#setAroundRadius}).
     */
    @Nullable
    public Integer automaticRadius;


    /**
     * Actual host name of the server that processed the request. (Our DNS supports automatic failover
     * and load balancing, so this may differ from the host name used in the request.)
     * <p>
     * Note: Only returned when {@link Query#getGetRankingInfo getRankingInfo} is true.
     */
    public String serverUsed;

    /**
     * The query string that will be searched, after normalization.
     * <p>
     * Note: Only returned when {@link Query#getGetRankingInfo getRankingInfo} is true.
     */
    public String parsedQuery;

    /**
     * Whether a timeout was hit when computing the facet counts.
     * When true, the counts will be interpolated (i.e. approximate). See also {@link SearchResults#exhaustiveFacetsCount exhaustiveFacetsCount}.
     * <p>
     * Note: Only returned when {@link Query#getGetRankingInfo getRankingInfo} is true.
     */
    public Boolean timeoutCounts;

    /**
     * Whether a timeout was hit when retrieving the hits. When true, some results may be missing.
     * <p>
     * Note: Only returned when {@link Query#getGetRankingInfo getRankingInfo} is true.
     */
    public Boolean timeoutHits;

    /**
     * Build a SearchResult object from a raw JSON response.
     *
     * @param content the JSON content to parse.
     * @throws IllegalStateException if the server response misses any mandatory field.
     */
    public SearchResults(@NonNull JSONObject content) {
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
        aroundLatLng = Query.parseLatLng(content.optString("aroundLatLng", null));
        serverUsed = content.optString("serverUsed", null);
        parsedQuery = content.optString("parsedQuery", null);
        facets = parseFacets(content.optJSONObject("facets"));
        disjunctiveFacets = parseFacets(content.optJSONObject("disjunctiveFacets"));
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

    @Override public String toString() {
        return content.toString();
    }
}
