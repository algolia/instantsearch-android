package com.algolia.instantsearch.helper.databinding;

import java.util.HashMap;

public class Result {
    private HashMap<String, String> attributesMap;
    public Result() {
        attributesMap = new HashMap<>();
    }

    public void set(String key, String value) {
        attributesMap.put(key, value);
    }

    public String get(String key) {
        return attributesMap.get(key);
    }
}
