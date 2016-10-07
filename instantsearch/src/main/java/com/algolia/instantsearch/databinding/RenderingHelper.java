package com.algolia.instantsearch.databinding;

import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RenderingHelper {

    private static RenderingHelper defaultRenderingHelper;

    @NonNull
    private final Set<String> attributeHighlights;
    @NonNull
    private final Map<String, Integer> attributeColors;

    public static RenderingHelper getDefault() {
        if (defaultRenderingHelper == null) {
            defaultRenderingHelper = new RenderingHelper();
        }
        return defaultRenderingHelper;
    }

    private RenderingHelper() {
        attributeHighlights = new HashSet<>();
        attributeColors = new HashMap<>();
    }

    final static String DEFAULT_COLOR = "@color/colorHighlighting";

    public @ColorRes int getHighlightColor(String attributeName) {
        return attributeColors.get(attributeName);
    }

    public boolean shouldHighlight(String attributeName) {
        return attributeHighlights.contains(attributeName);
    }

    /**
     * Set a color for this attribute's highlighting.
     *
     * @param attributeName the attribute to color.
     * @param colorId       a {@link ColorRes} to associate with this attribute.
     * @return the previous color associated with this attribute or {@code null} if there was none.
     */
    Integer addColor(String attributeName, @ColorRes int colorId) {
        return attributeColors.put(attributeName, colorId);
    }

    /**
     * Enable highlighting for this attribute.
     *
     * @param attributeName the attribute to color.
     * @return {@code true} if the attribute was not already highlighted, {@code false} otherwise.
     */
    boolean addHighlight(String attributeName) {
        return attributeHighlights.add(attributeName);
    }
}
