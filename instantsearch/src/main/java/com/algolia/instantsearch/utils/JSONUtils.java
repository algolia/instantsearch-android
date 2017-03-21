package com.algolia.instantsearch.utils;

import android.util.Log;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

import org.json.JSONObject;

import java.util.HashMap;

/** Provides helper function for manipulating {@link JSONObject JSONObjects}. */
public class JSONUtils {
    /**
     * Gets a string attribute from a json object given a path to traverse.
     *
     * @param record a JSONObject to traverse.
     * @param path   the json path to follow.
     * @return the attribute as a {@link String}, or null if it was not found.
     */
    public static String getStringFromJSONPath(JSONObject record, String path) {
        final Object object = getObjectFromJSONPath(record, path);
        return object == null ? null : object.toString();
    }

    /**
     * Gets a Map of attributes from a json object given a path to traverse.
     *
     * @param record a JSONObject to traverse.
     * @param path   the json path to follow.
     * @return the attributes as a {@link HashMap}, or null if it was not found.
     */
    public static HashMap<String, String> getMapFromJSONPath(JSONObject record, String path) {
        return getObjectFromJSONPath(record, path);
    }

    private static <T> T getObjectFromJSONPath(JSONObject record, String path) {
        final String json = record.toString();
        try {
            return JsonPath.read(json, path);
        } catch (PathNotFoundException exception) {
            Log.e("Algolia|JSONUtils", "Cannot find \"" + path + "\" in json:" + json);
            return null;
        }
    }
}
