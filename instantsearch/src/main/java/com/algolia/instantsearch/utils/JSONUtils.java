package com.algolia.instantsearch.utils;

import com.jayway.jsonpath.JsonPath;

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
            return JsonPath.read(record.toString(), path);
    }
}
