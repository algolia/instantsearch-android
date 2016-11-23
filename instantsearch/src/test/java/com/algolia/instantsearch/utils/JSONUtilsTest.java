package com.algolia.instantsearch.utils;

import com.algolia.instantsearch.InstantSearchTest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class JSONUtilsTest extends InstantSearchTest {

    @Test
    public void getSimpleAttribute() throws JSONException {
        JSONObject data = new JSONObject().put("foo", "42");
        assertEquals("Failed to get a simple String attribute", "42", JSONUtils.getStringFromJSONPath(data, "foo"));

        data = new JSONObject().put("foo", 42);
        assertEquals("Failed to get a simple Integer", "42", JSONUtils.getStringFromJSONPath(data, "foo"));

        data = new JSONObject().put("foo", new JSONArray().put(1).put(2));
        assertEquals("Failed to get a simple JSONArray", "[1,2]", JSONUtils.getStringFromJSONPath(data, "foo"));
    }

    @Test public void getNestedAttribute() throws JSONException {
        JSONObject data = new JSONObject().put("foo", new JSONObject().put("bar", "42"));
        assertEquals("Failed to get a nested attribute", "42", JSONUtils.getStringFromJSONPath(data, "foo.bar"));
    }
}
