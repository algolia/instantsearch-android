package com.algolia.instantsearch;

import com.algolia.instantsearch.model.NumericRefinement;
import com.algolia.instantsearch.model.SearchResults;
import com.algolia.instantsearch.views.AlgoliaResultsListener;
import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.Query;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.Locale;

public class SearcherTest extends InstantSearchTest {
    @Test
    public void hasHitsFalseIfEmpty() {
        boolean output = Searcher.hasHits(null);
        Assert.assertFalse("hasHits should return false on null input", output);
    }

    @Test
    public void hasHitsFalseIfNoHits() throws JSONException {
        JSONObject input = new JSONObject("{'name':'foo'}");
        boolean output = Searcher.hasHits(input);
        Assert.assertFalse("hasHits should return false on input with no hits", output);
    }

    @Test
    public void hasHitsFalseIfNullHitsArray() throws JSONException {
        JSONObject input = new JSONObject("{'hits':null}");
        boolean output = Searcher.hasHits(input);
        Assert.assertFalse("hasHits should return false on input with null hits array", output);
    }

    @Test
    public void hasHitsFalseIfNullHits() throws JSONException {
        JSONObject input = new JSONObject("{'hits':[null, null]}");
        boolean output = Searcher.hasHits(input);
        Assert.assertFalse("hasHits should return false on input with null hits", output);
    }

    @Test
    public void hasHitsTrueIfHits() throws JSONException {
        JSONObject input = new JSONObject("{'hits':[{'name':'foo'}]}");
        boolean output = Searcher.hasHits(input);
        Assert.assertTrue("hasHits should return true on input with some hits", output);
    }

    @Test
    public void canCancelPendingRequests() {
        final Client client = new Client(Helpers.app_id, Helpers.api_key);
        Searcher searcher = new Searcher(client.initIndex(Helpers.safeIndexName("test")));
        final AlgoliaResultsListener resultsListener = new AlgoliaResultsListener() {
            @Override public void onResults(SearchResults results, boolean isLoadingMore) {
                Assert.fail("The request should have been cancelled.");
            }

            @Override public void onError(Query query, AlgoliaException error) {
                Assert.fail("The request should have been cancelled.");
            }
        };
        searcher.registerListener(resultsListener);
        searcher.search();
        Assert.assertTrue("There should be a pending request", searcher.hasPendingRequests());
        searcher.cancelPendingRequests();
    }

    @Test
    public void numericRefinements() {
        final Client client = new Client(Helpers.app_id, Helpers.api_key);
        Searcher searcher = new Searcher(client.initIndex(Helpers.safeIndexName("test")));
        final NumericRefinement r = new NumericRefinement("attribute", NumericRefinement.OPERATOR_EQ, 42);
        final NumericRefinement r2 = new NumericRefinement("attribute", NumericRefinement.OPERATOR_NE, 42);
        final String formattedValue = String.format(Locale.US, "%f", 42f);

        searcher.addNumericRefinement(r);
        Assert.assertEquals("There should be a numeric refinement for attribute", r, searcher.getNumericRefinement(r.attribute, r.operator));
        Assert.assertEquals("Query filters should represent the refinement", "attribute=" + formattedValue, searcher.getQuery().getFilters());

        searcher.removeNumericRefinement(r);
        Assert.assertEquals("This numeric refinement should have been removed.", searcher.getNumericRefinement(r.attribute, r.operator), null);
        Assert.assertEquals("Query filters should be empty after removal", "", searcher.getQuery().getFilters());

        searcher.addNumericRefinement(r);
        searcher.addNumericRefinement(r2);
        Assert.assertEquals("Query filters should represent both refinements", "attribute=" + formattedValue + " AND attribute!=" + formattedValue, searcher.getQuery().getFilters());

        searcher.removeNumericRefinement(r.attribute);
        Assert.assertEquals("Both numeric refinements for this attribute should have been removed", searcher.getNumericRefinement(r.attribute, r.operator), null);
        Assert.assertEquals("Query filters should be empty after removal", "", searcher.getQuery().getFilters());

        searcher.addNumericRefinement(r);
        searcher.addNumericRefinement(r2);
        searcher.removeNumericRefinement(r.attribute, r.operator);
        Assert.assertEquals("The numeric refinement for this attribute/operator pair should have been removed", searcher.getNumericRefinement(r.attribute, r.operator), null);
        Assert.assertEquals("The numeric refinement for this attribute but other operator should have been kept", r2, searcher.getNumericRefinement(r2.attribute, r2.operator));
    }
}
