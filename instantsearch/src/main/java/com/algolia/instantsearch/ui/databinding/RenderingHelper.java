package com.algolia.instantsearch.ui.databinding;

import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Contains mappings between an attribute to highlight and its appropriate highlighting color.
 */
@SuppressWarnings("UnusedReturnValue")
public class RenderingHelper {
    final static String DEFAULT_COLOR = "@color/colorHighlighting";

    /** The default RenderingHelper, highlighting with the {@link RenderingHelper#DEFAULT_COLOR default color}. */
    private static RenderingHelper defaultRenderingHelper;

    /** The Set of attributes to highlight. */
    @NonNull
    private final Set<String> highlightedAttributes;
    /** A Map associating attributes with their respective highlighting {@link ColorRes color}. */
    @NonNull
    private final Map<String, Integer> attributeColors;

    /** Gets the {@link RenderingHelper#defaultRenderingHelper default RenderingHelper}. */
    public static RenderingHelper getDefault() {
        if (defaultRenderingHelper == null) {
            defaultRenderingHelper = new RenderingHelper();
        }
        return defaultRenderingHelper;
    }

    private RenderingHelper() {
        highlightedAttributes = new HashSet<>();
        attributeColors = new HashMap<>();
    }

    /**
     * Gets the highlighting color for a given attribute.
     *
     * @param attributeName the attribute's name.
     * @return the {@link ColorRes} associated with this attribute, or 0 if there is none.
     */
    public @ColorRes Integer getHighlightColor(String attributeName) {
        try {
            return attributeColors.get(attributeName);
        } catch (NullPointerException e) {
            return 0;
        }
    }

    /**
     * Checks if an attribute should be highlighted.
     *
     * @param attributeName the attribute's name.
     * @return {@code true} if the attribute was marked for highlighting.
     */
    public boolean shouldHighlight(String attributeName) {
        return highlightedAttributes.contains(attributeName);
    }

    /**
     * Sets a color for this attribute's highlighting.
     *
     * @param attributeName the attribute to color.
     * @param colorId       a {@link ColorRes} to associate with this attribute.
     * @return the previous color associated with this attribute or {@code null} if there was none.
     */
    Integer addColor(String attributeName, @ColorRes int colorId) {
        return attributeColors.put(attributeName, colorId);
    }

    /**
     * Enables highlighting for this attribute.
     *
     * @param attributeName the attribute to color.
     * @return {@code true} if the attribute was not already highlighted, {@code false} otherwise.
     */
    boolean addHighlight(String attributeName) {
        return highlightedAttributes.add(attributeName);
    }
}
