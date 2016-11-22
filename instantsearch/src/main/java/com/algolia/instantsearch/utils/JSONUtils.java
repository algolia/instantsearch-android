package com.algolia.instantsearch.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtils {
    public static String getStringFromJSONPath(JSONObject record, String path) {
        return getObjectFromJSONPath(record, path).toString();
    }

    public static JSONObject getJSONObjectFromJSONPath(JSONObject record, String path) {
        return (JSONObject) getObjectFromJSONPath(record, path);
    }

    private static Object getObjectFromJSONPath(JSONObject record, String path) {
        try {
            if (path.contains(".")) { // "user.name"
                while (path.contains(".")) {
                    String[] paths = path.split("\\."); //["user", "name"]
                    if (paths.length > 1) {
                        record = record.getJSONObject(paths[0]); // getJSONObject("user")
                        path = path.substring(paths[0].length() + 1); // "user.|name"
                    } else { // ["name"]
                        path = paths[0]; // "name"
                        break;
                    }
                }
            }
            return record.get(path);
        } catch (JSONException e) {
            throw new IllegalStateException("Error while processing JSONObject \"" + record + "\": " + e.getMessage(), e);
        }
    }
}
