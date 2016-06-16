package com.algolia.instantsearch;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class SearchHelperTest extends InstantSearchTest {
    @Test
    public void hasHitsFalseIfEmpty() {
        JSONObject input = null;
        boolean output = SearchHelper.hasHits(input);
        Assert.assertFalse("hasHits should return false on null input", output);
    }

    @Test
    public void hasHitsFalseIfNoHits() throws JSONException {
        JSONObject input = new JSONObject("{'name':'foo'}");
        boolean output = SearchHelper.hasHits(input);
        Assert.assertFalse("hasHits should return false on input with no hits", output);
    }

    @Test
    public void hasHitsFalseIfNullHitsArray() throws JSONException {
        JSONObject input = new JSONObject("{'hits':null}");
        boolean output = SearchHelper.hasHits(input);
        Assert.assertFalse("hasHits should return false on input with null hits array", output);
    }

    @Test
    public void hasHitsFalseIfNullHits() throws JSONException {
        JSONObject input = new JSONObject("{'hits':[null, null]}");
        boolean output = SearchHelper.hasHits(input);
        Assert.assertFalse("hasHits should return false on input with null hits", output);
    }

    @Test
    public void hasHitsTrueIfHits() throws JSONException {
        JSONObject input = new JSONObject("{'hits':[{'name':'foo'}]}");
        boolean output = SearchHelper.hasHits(input);
        Assert.assertTrue("hasHits should return true on input with some hits", output);
    }
}
