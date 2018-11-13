/*
 * Copyright (c) 2018 Algolia
 * http://www.algolia.com/
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.algolia.instantsearch.core.searchclient;

import com.algolia.instantsearch.core.model.SearchResults;
import com.algolia.search.saas.Query;
import com.algolia.search.saas.Request;

import org.json.JSONObject;

import java.util.Collection;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface Transformable<Parameters, Results> {

    /***
     * Performs a search for the given Parameters, returning Results or Error
     *
     * @param query Search parameters. May be null to use an empty query.
     * @param completionHandler The listener that will be notified of the request's outcome.
     */
    Request search(@Nullable Parameters query, @Nullable SearchResultsHandler<Results> completionHandler);

    /***
     * Transforms the Algolia params to custom backend params.
     *
     * @param query Search parameters. May be an empty query.
     * @return The search parameters with the custom format required by the custom back-end implementation
     */
    Parameters map(@NonNull Query query);

    /***
     * Transforms the Algolia params + disjunctive refinements to custom backend params.
     *
     * @param query Search parameters. May be null to use an empty query.
     * @param disjunctiveFacets List of disjunctive facets.
     * @param refinements The current refinements, mapping facet names to a list of values.
     * @return The search parameters with the custom format required by the custom back-end implementation
     */
    Parameters map(@NonNull Query query, @NonNull final Collection<String> disjunctiveFacets, @NonNull final Map<String, ? extends Collection<String>> refinements);

    /***
     * Transforms the custom backend result to Algolia results.
     *
     * @param results Content that was returned by your custom API (in case of success).
     * @return json object from your custom results. It has to match the parameters defined in {@link SearchResults#SearchResults(JSONObject)}
     */
    JSONObject map(@NonNull Results results);


    /***
     * Performs a search in the values for a given facet
     *
     * @param query Search parameters. May be null to use an empty query.
     * @param completionHandler The listener that will be notified of the request's outcome.
     */
    Request searchForFacetValues(@NonNull Parameters query, @Nullable SearchResultsHandler<Results> completionHandler);


    /***
     * Transforms the Algolia facet value params to custom backend params.
     * @param query Search parameters. May be null to use an empty query.
     * @param facetName The name of the facet to search.
     * @param matchingText The text to search for in the facet's values.
     * @return The search parameters with the custom format required by the custom back-end implementation
     */
    Parameters map(@Nullable Query query, @NonNull String facetName, @NonNull String matchingText);
}
