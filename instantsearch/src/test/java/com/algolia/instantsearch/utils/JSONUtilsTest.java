package com.algolia.instantsearch.utils;

import com.algolia.instantsearch.InstantSearchTest;
import com.jayway.jsonpath.InvalidPathException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

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

    @Test
    public void getNestedAttribute() throws JSONException {
        JSONObject data = new JSONObject().put("foo", new JSONObject().put("bar", "42"));
        assertEquals("Failed to get a nested attribute", "42", JSONUtils.getStringFromJSONPath(data, "foo.bar"));
    }

    @Test
    public void getArrayAttribute() throws JSONException {
        JSONObject data = new JSONObject().put("foo", new JSONArray().put("42"));
        assertEquals("Failed to dereference an array attribute", "42", JSONUtils.getStringFromJSONPath(data, "foo[0]"));

        data = new JSONObject().put("foo", new JSONArray().put(new JSONObject().put("bar", "42")));
        assertEquals("Failed to get the attribute of an array item", "42", JSONUtils.getStringFromJSONPath(data, "foo[0].bar"));

        data = new JSONObject().put("foo", new JSONObject().put("bar", new JSONArray().put(new JSONObject().put("baz", "42"))));
        assertEquals("Failed to get the attribute of an array item", "42", JSONUtils.getStringFromJSONPath(data, "foo.bar[0].baz"));
    }

    @Test
    public void getArrayArray() throws JSONException {
        JSONObject data = new JSONObject().put("foo", new JSONArray().put(new JSONArray().put("42")));
        assertEquals("Failed to get the attribute of an array item", "42", JSONUtils.getStringFromJSONPath(data, "foo[0][0]"));
    }

    @Test(expected = InvalidPathException.class)
    public void invalidArrayIndex() throws JSONException {
        JSONObject data = new JSONObject().put("foo", new JSONObject().put("bar", new JSONArray().put("42")));
        JSONUtils.getStringFromJSONPath(data, "foo.bar[baz]");
        fail("Failed to throw on invalid array index.");
    }
}
