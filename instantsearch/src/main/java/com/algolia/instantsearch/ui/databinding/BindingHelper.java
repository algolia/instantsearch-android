package com.algolia.instantsearch.ui.databinding;

import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.View;

import com.algolia.instantsearch.model.Errors;

/**
 * Contains every {@link BindingAdapter} used for binding record attributes to Views.
 */
public class BindingHelper {
    private static final SparseArray<String> bindings = new SparseArray<>();

    @SuppressWarnings("unused") // called via Data Binding
    @Deprecated // Should not be used by library users
    @BindingAdapter({"attribute"})
    public static void bindAttribute(@NonNull View view, String attribute) {
        final int id = view.getId();
        if (notAlreadyMapped(id)) { // only map when you see a view for the first time.
            mapAttribute(attribute, id);
        }
    }

    @SuppressWarnings({"unused"}) // called via Data Binding
    @Deprecated // Should not be used by library users
    @BindingAdapter({"attribute", "highlighted"})
    public static void bindHighlighted(@NonNull View view, String attribute, Boolean isHighlighted) {
        // Bind attribute, enable highlight with default color
        bindAndHighlight(view, attribute, RenderingHelper.DEFAULT_COLOR);
    }

    @SuppressWarnings({"unused"}) // called via Data Binding
    @Deprecated // Should not be used by library users
    @BindingAdapter({"attribute", "highlightingColor"})
    public static void bindHighlighted(@NonNull View view, String attribute, @NonNull String colorStr) {
        // Bind attribute, enable highlight with color
        bindAndHighlight(view, attribute, colorStr);
    }

    @SuppressWarnings({"unused"}) // called via Data Binding
    @Deprecated // Should not be used by library users
    @BindingAdapter({"attribute", "highlighted", "highlightingColor"})
    public static void bindHighlighted(@NonNull View view, String attribute, Boolean isHighlighted, @NonNull String colorStr) {
        // Bind attribute, enable highlight with color
        bindAndHighlight(view, attribute, colorStr);
    }

    @SuppressWarnings({"unused", "UnusedParameters"}) // called via Data Binding and throws
    @Deprecated // Should not be used by library users
    @BindingAdapter({"highlighted"})
    public static void bindInvalid(@NonNull View view, Boolean isHighlighted) {
        throwBindingError(view, Errors.BINDING_HIGHLIGHTED_NO_ATTR);
    }

    @SuppressWarnings({"unused", "UnusedParameters"}) // called via Data Binding and throws
    @Deprecated // Should not be used by library users
    @BindingAdapter({"highlightingColor"})
    public static void bindInvalid(@NonNull View view, @ColorRes int color) {
        throwBindingError(view, Errors.BINDING_HIGHLIGHTING_NO_ATTR);
    }

    @SuppressWarnings({"unused", "UnusedParameters"}) // called via Data Binding and throws
    @Deprecated // Should not be used by library users
    @BindingAdapter({"highlighted", "highlightingColor"})
    public static void bindInvalid(@NonNull View view, Boolean isHighlighted, String colorStr) {
        throwBindingError(view, Errors.BINDING_NO_ATTR);
    }

    private static boolean notAlreadyMapped(int id) {
        return bindings.get(id) == null;
    }

    private static void mapAttribute(String attribute, int viewId) {
        if (viewId == -1) {
            throw new IllegalStateException(String.format(Errors.BINDING_VIEW_NO_ID, attribute));
        }
        bindings.put(viewId, attribute);
    }

    private static void throwBindingError(@NonNull View view, String message) {
        final Resources r = view.getContext().getResources();
        int id = view.getId();
        String viewName = r.getResourcePackageName(id) + ":" + r.getResourceTypeName(id) + "/" + r.getResourceEntryName(id);
        throw new IllegalStateException("Cannot bind " + viewName + ": " + message);
    }

    private static void bindAndHighlight(@NonNull View view, String attribute, @NonNull String colorStr) {
        if (notAlreadyMapped(view.getId())) {
            final String[] split = colorStr.split("/");
            final String identifierType = split[0];
            final String colorName = split[1];

            final int colorId;
            switch (identifierType) {
                case "@android:color":
                    colorId = Resources.getSystem().getIdentifier(colorName, "color", "android");
                    break;
                case "@color":
                    colorId = view.getResources().getIdentifier(colorName, "color", view.getContext().getPackageName());
                    break;
                default:
                    throw new IllegalStateException(Errors.BINDING_COLOR_INVALID);
            }

            bindAttribute(view, attribute);
            final RenderingHelper renderingHelper = RenderingHelper.getDefault();
            renderingHelper.addHighlight(attribute);
            renderingHelper.addColor(attribute, colorId);
        }
    }

    /**
     * Gets the current bindings.
     *
     * @return a SparseArray mapping each View's id to its attribute name.
     */
    public static SparseArray<String> getBindings() {
        return bindings;
    }
}
