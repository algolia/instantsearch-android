package com.algolia.instantsearch.ui.databinding;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.View;

import com.algolia.instantsearch.model.Errors;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains the {@link BindingAdapter} used for gathering {@link BindingHelper#bindings bound attributes}.
 */
public class BindingHelper {
    private static final HashMap<Pair<String, String>, HashMap<Integer, String>> bindings = new HashMap<>();

    /**
     * @deprecated This should only be used internally by InstantSearch.
     */
    @SuppressWarnings("unused") // called via Data Binding
    @Deprecated // Should not be used by library users
    @BindingAdapter(value = {"attribute", "highlighted", "highlightingColor", "index", "variant"}, requireAll = false)
    public static void bindAttribute(@NonNull View view, @Nullable String attribute,
                                     @Nullable Boolean highlighted,
                                     @Nullable @ColorRes Integer color,
                                     @Nullable String index, @Nullable String variant) {
        if (attribute == null) {
            throwBindingError(view, Errors.BINDING_NO_ATTRIBUTE);
        } else {
            bindAttribute(view, attribute, index, variant);
        }

        if ((highlighted != null && highlighted) // either highlighted == true
                || (color != null && (highlighted == null || !highlighted))) // or color && highlighted != false
        {
            highlight(view, attribute, color != null ? color : RenderingHelper.DEFAULT_COLOR);
        }
    }

    /**
     * @deprecated This should only be used internally by InstantSearch.
     */
    @Deprecated // Should not be used by library users
    public static void bindAttribute(@NonNull View view, @NonNull String attribute, @Nullable String index, @Nullable String variant) {
        final int id = view.getId();
        if (notAlreadyMapped(id, index, variant)) { // only map when you see a view for the first time.
            mapAttribute(attribute, id, index, variant);
        }
    }

    /**
     * Gets the current bindings.
     *
     * @return a SparseArray mapping each View's id to its attribute name.
     */
    public static HashMap<Integer, String> getBindings(Pair<String, String> indexVariant) {
        return bindings.get(indexVariant);
    }

    /**
     * Returns the index name and variant associated with the view.
     *
     * @param view a View that is associated to an index through databinding.
     * @return a Pair containing the index name and the variant name (both being {@code null} if unspecified).
     * @throws IllegalArgumentException if the view is not part of a databinding layout.
     */
    public static @NonNull Pair<String, String> getIndexVariantForView(View view) {
        for (Map.Entry<Pair<String, String>, HashMap<Integer, String>> entry : bindings.entrySet()) {
            if (entry.getValue().containsKey(view.getId())) {
                return entry.getKey();
            }
        }
        throw new IllegalArgumentException("View " + getViewName(view) + " was not bound to any data.");
    }

    private static boolean notAlreadyMapped(int id, @Nullable String index, @Nullable String variant) {
        final HashMap<Integer, String> indexMap = bindings.get(new Pair<>(index, variant));
        return indexMap == null || indexMap.get(id) == null;
    }

    @SuppressLint("UseSparseArrays") // Using HashMap for O(1) containsKey in getIndexVariantForView
    private static void mapAttribute(String attribute, int viewId, @Nullable String index, @Nullable String variant) {
        if (viewId == -1) {
            throw new IllegalStateException(String.format(Errors.BINDING_VIEW_NO_ID, attribute));
        }
        final Pair<String, String> key = new Pair<>(index, variant);
        HashMap<Integer, String> indexMap = bindings.get(key);
        if (indexMap == null) {
            indexMap = new HashMap<>();
            bindings.put(key, indexMap);
        }
        indexMap.put(viewId, attribute);
    }

    private static void throwBindingError(@NonNull View view, String message) {
        String viewName = getViewName(view);
        throw new IllegalStateException("Cannot bind " + viewName + ": " + message);
    }

    @NonNull private static String getViewName(@NonNull View view) {
        final Resources r = view.getContext().getResources();
        int id = view.getId();
        return r.getResourcePackageName(id) + ":" + r.getResourceTypeName(id) + "/" + r.getResourceEntryName(id);
    }

    private static void highlight(@NonNull View view, String attribute, @ColorRes int color) {
        RenderingHelper.getDefault().addHighlight(view, attribute, color);
    }
}
