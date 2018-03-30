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

package com.algolia.instantsearch.transformer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.algolia.instantsearch.model.SearchResults;
import com.algolia.search.saas.Query;

import org.json.JSONObject;

import java.util.Collection;
import java.util.Map;

public interface Transformable<Parameters, Results> {

    /***
     * Search operation
     *
     * @param query
     * @param completionHandler
     */
    void search(@Nullable Parameters query, @Nullable SearchResultsHandler<Results> completionHandler);

    /***
     * Transforms the Algolia params to custom backend params.
     *
     * @param query
     * @return
     */
    Parameters map(@NonNull Query query);

    /***
     * Transforms the Algolia params + disjunctive refinements to custom backend params.
     *
     * @param query
     * @param disjunctiveFacets
     * @param refinements
     * @return
     */
    Parameters map(@NonNull Query query, @NonNull final Collection<String> disjunctiveFacets, @NonNull final Map<String, ? extends Collection<String>> refinements);

    /***
     * Transforms the custom backend result to an Algolia result.
     *
     * @param results
     * @return
     */
    JSONObject map(@NonNull Results results);


    /***
     * Search for facet value operation
     *
     * @param query
     * @param completionHandler
     */
    void searchForFacetValues(@NonNull Parameters query, @Nullable SearchResultsHandler<Results> completionHandler);


    /***
     * Transforms the Algolia facet value params to custom backend params.
     * @param query
     * @param facetName
     * @param matchingText
     * @return
     */
    Parameters map(@Nullable Query query, @NonNull String facetName, @NonNull String matchingText);
}
