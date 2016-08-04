package com.algolia.instantsearch;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.algolia.instantsearch.views.AlgoliaResultsListener;
import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.Query;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

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
            @Override public void onUpdateView(@Nullable JSONObject hits, boolean isLoadingMore) {
                Assert.fail("The request should have been cancelled.");
            }

            @Override public void onError(Query query, AlgoliaException error) {
                Assert.fail("The request should have been cancelled.");
            }

            @Override public void onInit(@NonNull Searcher searcher) {

            }

            @Override public void onReset() {

            }
        };
        searcher.registerListener(resultsListener);
        searcher.search();
        Assert.assertTrue("There should be a pending request", searcher.hasPendingRequests());
        searcher.cancelPendingRequests();
    }
}
