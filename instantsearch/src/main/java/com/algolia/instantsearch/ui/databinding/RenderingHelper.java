package com.algolia.instantsearch.ui.databinding;

import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.util.Pair;
import android.view.View;

import com.algolia.instantsearch.R;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Contains mappings between a view+attribute to highlight and its appropriate highlighting color.
 */
@SuppressWarnings("UnusedReturnValue")
public class RenderingHelper {
    @ColorRes final static int DEFAULT_COLOR = R.color.colorHighlighting;

    /** The default RenderingHelper, highlighting with the {@link RenderingHelper#DEFAULT_COLOR default color}. */
    private static RenderingHelper defaultRenderingHelper;

    /** The Set of view IDs/attribute pairs to highlight. */
    @NonNull private final Set<Pair<Integer, String>> highlightedAttributes;
    /** A Map associating view IDs/attribute pairs with their respective highlighting {@link ColorInt color}. */
    @NonNull private final Map<Pair<Integer, String>, Integer> attributeColors;

    /**
     * Gets the {@link RenderingHelper#defaultRenderingHelper default RenderingHelper}.
     *
     * @return the RenderingHelper instance, eventually creating it.
     */
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
     * Gets the highlighting color for a given view/attribute pair.
     *
     * @param view      the view using this attribute.
     * @param attribute the attribute's name.
     * @return the {@link ColorInt} associated with this view/attribute pair, or 0 if there is none.
     */
    public @ColorInt Integer getHighlightColor(View view, String attribute) {
        try {
            return attributeColors.get(new Pair<>(view.getId(), attribute));
        } catch (NullPointerException e) {
            return 0;
        }
    }

    /**
     * Checks if an attribute should be highlighted in a view.
     *
     * @param view      the view using this attribute.
     * @param attribute the attribute's name.
     * @return {@code true} if the attribute was marked for highlighting.
     */
    public boolean shouldHighlight(View view, String attribute) {
        return highlightedAttributes.contains(new Pair<>(view.getId(), attribute));
    }

    /**
     * Enables highlighting for this view/attribute pair.
     *
     * @param attribute the attribute to color.
     * @param colorId   a {@link ColorInt} to associate with this attribute.
     * @return the previous color associated with this attribute or {@code null} if there was none.
     */
    Integer addHighlight(View view, String attribute, @ColorInt int colorId) {
        final Pair<Integer, String> pair = new Pair<>(view.getId(), attribute);
        highlightedAttributes.add(pair);
        return attributeColors.put(pair, colorId);
    }
}
