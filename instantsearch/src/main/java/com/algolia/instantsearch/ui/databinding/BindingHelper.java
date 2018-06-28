package com.algolia.instantsearch.ui.databinding;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import com.algolia.instantsearch.R;
import com.algolia.instantsearch.model.Errors;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Contains the {@link BindingAdapter} used for gathering {@link BindingHelper#bindings bound attributes}.
 */
public class BindingHelper {
    /**
     * Associates a variant with its bindings (associating each databound view's id to its attribute).
     */
    @NonNull
    private static final HashMap<String, HashMap<Integer, String>> bindings = new HashMap<>();
    @NonNull
    private static final SparseArray<String> prefixes = new SparseArray<>();
    @NonNull
    private static final SparseArray<String> suffixes = new SparseArray<>();

    /**
     * @deprecated This should only be used internally by InstantSearch.
     */
    @SuppressWarnings("unused") // called via Data Binding
    @Deprecated // Should not be used by library users
    @BindingAdapter(value = {"attribute", "highlighted", "highlightColor", "variant", "prefix", "suffix"}, requireAll = false)
    public static void bindAttribute(@NonNull View view, @Nullable String attribute,
                                     @Nullable Boolean highlighted,
                                     @Nullable @ColorInt Integer color,
                                     @Nullable String variant,
                                     @Nullable String prefix, @Nullable String suffix) {
        if (attribute == null) {
            throwBindingError(view, Errors.BINDING_NO_ATTRIBUTE);
        } else {
            bindAttribute(view, attribute, variant);
        }

        if ((highlighted != null && highlighted) // either highlighted == true
                || (color != null && (highlighted == null || !highlighted))) // or color && highlighted != false
        {
            highlight(view, attribute, color != null ? color : view.getResources().getColor(RenderingHelper.DEFAULT_COLOR));
        }

        if (prefix != null) {
            prefixes.put(view.getId(), prefix);
        }
        if (suffix != null) {
            suffixes.put(view.getId(), suffix);
        }
    }

    /**
     * @deprecated This should only be used internally by InstantSearch.
     */
    @Deprecated // Should not be used by library users
    public static void bindAttribute(@NonNull View view, @NonNull String attribute, @Nullable String variant) {
        final int id = view.getId();
        if (notAlreadyMapped(id, variant)) { // only map when you see a view for the first time.
            mapAttribute(attribute, id, variant);
        }
    }

    /**
     * Gets the current bindings.
     *
     * @return a SparseArray mapping each View's id to its attribute name.
     */
    public static HashMap<Integer, String> getBindings(String indexVariant) {
        return bindings.get(indexVariant);
    }

    /**
     * Associates programmatically a view with its variant, taking it from its `variant` layout attribute.
     *
     * @param view  any existing view.
     * @param attrs the view's AttributeSet.
     * @return the previous variant for this view, if any.
     */
    @Nullable
    public static String setVariantForView(@NonNull View view, @NonNull AttributeSet attrs) {
        String previousVariant;
        final TypedArray styledAttributes = view.getContext().getTheme()
                .obtainStyledAttributes(attrs, R.styleable.View, 0, 0);
        try {
            previousVariant = setVariantForView(view, styledAttributes
                    .getString(R.styleable.View_variant));
        } finally {
            styledAttributes.recycle();
        }

        return previousVariant;
    }

    /**
     * Associates programmatically a view with a variant.
     *
     * @param view any existing view.
     * @return the previous variant for this view, if any.
     */
    @Nullable
    public static String setVariantForView(@NonNull View view, @Nullable String variant) {
        String previousVariant = null;
        String previousAttribute = null;
        for (Map.Entry<String, HashMap<Integer, String>> entry : bindings.entrySet()) {
            if (entry.getValue().containsKey(view.getId())) {
                previousVariant = entry.getKey();
                previousAttribute = entry.getValue().get(view.getId());
                bindings.remove(previousVariant);
                break;
            }
        }
        mapAttribute(previousAttribute, view.getId(), variant);
        return previousVariant;
    }

    /**
     * Returns the variant associated with the view.
     *
     * @param view a View that is associated to an index through databinding.
     * @return the variant name or {@code null} if unspecified.
     * @throws IllegalArgumentException if the view is not part of a databinding layout.
     */
    @Nullable
    public static String getVariantForView(@NonNull View view) {
        for (Map.Entry<String, HashMap<Integer, String>> entry : bindings.entrySet()) {
            if (entry.getValue().containsKey(view.getId())) {
                return entry.getKey();
            }
        }
        return null;
    }

    private static boolean notAlreadyMapped(int id, @Nullable String variant) {
        final HashMap<Integer, String> indexMap = bindings.get(variant);
        return indexMap == null || indexMap.get(id) == null;
    }

    public static boolean isBound(int viewId) {
        final Set<Map.Entry<String, HashMap<Integer, String>>> allVariants = bindings.entrySet();
        for (Map.Entry<String, HashMap<Integer, String>> variantMap : allVariants) {
            if (variantMap.getValue().keySet().contains(viewId)) {
                return true;
            }
        }
        return false;
    }

    @SuppressLint("UseSparseArrays") // Using HashMap for O(1) containsKey in getVariantForView
    private static void mapAttribute(String attribute, int viewId, @Nullable String variant) {
        if (viewId == -1) {
            throw new IllegalStateException(String.format(Errors.BINDING_VIEW_NO_ID, attribute));
        }
        HashMap<Integer, String> variantMap = bindings.get(variant);
        if (variantMap == null) {
            variantMap = new HashMap<>();
            bindings.put(variant, variantMap);
        }
        variantMap.put(viewId, attribute);
    }

    private static void throwBindingError(@NonNull View view, String message) {
        String viewName = getViewName(view);
        throw new IllegalStateException("Cannot bind " + viewName + ": " + message);
    }

    @NonNull
    private static String getViewName(@NonNull View view) {
        final Resources r = view.getContext().getResources();
        int id = view.getId();
        return r.getResourcePackageName(id) + ":" + r.getResourceTypeName(id) + "/" + r.getResourceEntryName(id);
    }

    private static void highlight(@NonNull View view, String attribute, @ColorInt int color) {
        RenderingHelper.getDefault().addHighlight(view, attribute, color);
    }

    /**
     * Gets the full version of an attribute, with its eventual prefix and suffix.
     *
     * @param view              the view mapped to this attribute.
     * @param rawAttributeValue the raw value of the attribute.
     * @return the attribute value, prefixed and suffixed if any was specified on the view.
     */
    @NonNull
    public static String getFullAttribute(@NonNull View view, @Nullable String rawAttributeValue) {
        String attribute = "";
        final String prefix = getPrefix(view);
        final String suffix = getSuffix(view);
        if (prefix != null) {
            attribute = prefix;
        }
        attribute += rawAttributeValue;
        if (suffix != null) {
            attribute += suffix;
        }
        return attribute;
    }

    /**
     * Gets the prefix for a given view, if any.
     *
     * @param view a view mapped to an attribute.
     * @return the prefix for this view, or {@code null} if none was set.
     */
    public static @Nullable
    String getSuffix(@NonNull View view) {
        return suffixes.get(view.getId());
    }

    /**
     * Gets the suffix for a given view, if any.
     *
     * @param view a view mapped to an attribute.
     * @return the suffix for this view, or {@code null} if none was set.
     */
    public static @Nullable
    String getPrefix(@NonNull View view) {
        return prefixes.get(view.getId());
    }
}
