/*
 *
 *  * Copyright (c) 2018 Algolia
 *  * http://www.algolia.com/
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be included in
 *  * all copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  * THE SOFTWARE.
 *
 */

package com.algolia.instantsearch.core.searchclient;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Query;
import com.algolia.search.saas.Request;
import com.algolia.search.saas.RequestOptions;
import com.algolia.search.saas.Searchable;

import org.json.JSONObject;

import java.util.Collection;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/***
 * Base class used to implement a custom backend with Algolia
 * You need to make sure to implement the following method:
 * {@link SearchClient#search(Object, SearchResultsHandler)}
 * {@link SearchClient#map(Query)}
 * {@link SearchClient#map(Object)}
 */
public abstract class SearchClient<Parameters, Results> extends Searchable implements Transformable<Parameters, Results> {

    @Override
    public Request search(@Nullable Parameters query, @Nullable SearchResultsHandler<Results> completionHandler) {
        throw new UnsupportedOperationException("This method was not implemented yet; override it if you need it");
    }

    @Override
    public Request searchForFacetValues(@NonNull Parameters query, @Nullable SearchResultsHandler<Results> completionHandler) {
        return search(query, completionHandler);
    }


    @Override
    public Parameters map(@NonNull Query query) {
        throw new UnsupportedOperationException("This method was not implemented yet; override it if you need it");
    }

    @Override
    public Parameters map(@NonNull Query query, @NonNull Collection<String> disjunctiveFacets, @NonNull Map<String, ? extends Collection<String>> refinements) {
        throw new UnsupportedOperationException("This method was not implemented yet; override it if you need it");
    }

    @Override
    public JSONObject map(@NonNull Results results) {
        throw new UnsupportedOperationException("This method was not implemented yet; override it if you need it");
    }

    @Override
    public Parameters map(@Nullable Query query, @NonNull String facetName, @NonNull String matchingText) {
        return null;
    }

    @Override
    public Request searchAsync(@Nullable Query query, @Nullable RequestOptions requestOptions, @Nullable final CompletionHandler completionHandler) {
        Parameters params = this.map(query);

        Request request = search(params, new SearchResultsHandler<Results>() {
            @Override
            public void requestCompleted(final Results content, final Exception error) {
                if (error != null) {
                    completionHandler.requestCompleted(null, new AlgoliaException((error.getMessage())));
                } else {
                    JSONObject jsonObject = map(content);
                    completionHandler.requestCompleted(jsonObject, null);
                }
            }
        });

        return request;
    }

    @Override
    public Request searchDisjunctiveFacetingAsync(@NonNull Query query, @NonNull Collection<String> disjunctiveFacets, @NonNull Map<String, ? extends Collection<String>> refinements, @Nullable RequestOptions requestOptions, @NonNull final CompletionHandler completionHandler) {
        Parameters params = this.map(query, disjunctiveFacets, refinements);

        Request request = search(params, new SearchResultsHandler<Results>() {
            @Override
            public void requestCompleted(final Results content, final Exception error) {
                if (error != null) {
                    completionHandler.requestCompleted(null, new AlgoliaException((error.getMessage())));
                } else {
                    JSONObject jsonObject = map(content);
                    completionHandler.requestCompleted(jsonObject, null);
                }
            }
        });

        return request;
    }

    @Override
    public Request searchForFacetValuesAsync(@NonNull String facetName, @NonNull String facetText, @Nullable Query query, @Nullable RequestOptions requestOptions, @NonNull final CompletionHandler completionHandler) {
        Parameters params = this.map(query, facetName, facetText);

        Request request = searchForFacetValues(params, new SearchResultsHandler<Results>() {
            @Override
            public void requestCompleted(final Results content, final Exception error) {
                if (error != null) {
                    completionHandler.requestCompleted(null, new AlgoliaException((error.getMessage())));
                } else {
                    JSONObject jsonObject = map(content);
                    completionHandler.requestCompleted(jsonObject, null);
                }
            }
        });

        return request;
    }
}

