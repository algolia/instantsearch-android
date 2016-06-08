package com.algolia.instantsearch;

import android.support.annotation.ColorRes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RenderingHelper {

    private static final Set<String> attributeHighlights = new HashSet<>();
    private static final Map<String, Integer> attributeColors = new HashMap<>();

    static final String DEFAULT_COLOR = "@color/highlightingColor";

    public static
    @ColorRes
    int getHighlightColor(String attributeName) {
        return attributeColors.get(attributeName);
    }

    public static boolean shouldHighlight(String attributeName) {
        return attributeHighlights.contains(attributeName);
    }

    protected static Integer addColor(String attributeName, int colorId) {
        return attributeColors.put(attributeName, colorId);
    }

    protected static boolean addHighlight(String attributeName) {
        return attributeHighlights.add(attributeName);
    }
}
