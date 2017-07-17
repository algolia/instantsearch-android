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
     * @param attribute the attribute's name.
     * @return the {@link ColorRes} associated with this attribute, or 0 if there is none.
     */
    public @ColorRes Integer getHighlightColor(String attribute) {
        try {
            return attributeColors.get(attribute);
        } catch (NullPointerException e) {
            return 0;
        }
    }

    /**
     * Checks if an attribute should be highlighted.
     *
     * @param attribute the attribute's name.
     * @return {@code true} if the attribute was marked for highlighting.
     */
    public boolean shouldHighlight(String attribute) {
        return highlightedAttributes.contains(attribute);
    }

    /**
     * Sets a color for this attribute's highlighting.
     *
     * @param attribute the attribute to color.
     * @param colorId   a {@link ColorRes} to associate with this attribute.
     * @return the previous color associated with this attribute or {@code null} if there was none.
     */
    Integer addColor(String attribute, @ColorRes int colorId) {
        return attributeColors.put(attribute, colorId);
    }

    /**
     * Enables highlighting for this attribute.
     *
     * @param attribute the attribute to color.
     * @return {@code true} if the attribute was not already highlighted, {@code false} otherwise.
     */
    boolean addHighlight(String attribute) {
        return highlightedAttributes.add(attribute);
    }
}
