package com.algolia.instantsearch.utils;

import android.os.Build;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class DevUtils {
    public static JSONArray removeFrom(JSONArray array, int index) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            array.remove(index);
        } else {
            array = myJSONArrayRemove(array, index);

        }
        return array;
    }

    private static JSONArray myJSONArrayRemove(JSONArray array, int index) {
        // Create list of JSONObject, remove the ith item and rebuild the JSONArray
        final int len = array.length();
        final ArrayList<JSONObject> list = new ArrayList<>(len);
        for (int j = 0; j < len; j++) {
            final JSONObject obj = array.optJSONObject(j);
            if (obj != null) {
                list.add(obj);
            }
        }
        list.remove(index);

        array = new JSONArray();
        for (final JSONObject obj : list) {
            array.put(obj);
        }
        return array;
    }
}
