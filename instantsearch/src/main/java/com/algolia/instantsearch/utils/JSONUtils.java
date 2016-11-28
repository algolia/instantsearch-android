package com.algolia.instantsearch.utils;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

import org.json.JSONObject;

import java.util.HashMap;

public class JSONUtils {
    public static String getStringFromJSONPath(JSONObject record, String path) {
        return getObjectFromJSONPath(record, path).toString();
    }

    public static HashMap<String, String> getJSONObjectFromJSONPath(JSONObject record, String path) {
        return getObjectFromJSONPath(record, path);
    }

    private static <T> T getObjectFromJSONPath(JSONObject record, String path) {
        final String json = record.toString();
        try {
            return JsonPath.read(json, path);
        } catch (PathNotFoundException exception) {
            throw new IllegalArgumentException("Cannot find \"" + path + "\" in json:" + json, exception);
        }
    }
}
