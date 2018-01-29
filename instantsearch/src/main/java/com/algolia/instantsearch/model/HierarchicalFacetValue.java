package com.algolia.instantsearch.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes a faceted attribute's value with its children.
 */
public class HierarchicalFacetValue extends FacetValue {

    /** Value to be displayed. */
    @NonNull final public String displayValue;

    /** Children facet values. */
    public List<HierarchicalFacetValue> children;

    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public HierarchicalFacetValue(@NonNull String value, @NonNull String displayValue, int count, @Nullable List<HierarchicalFacetValue> children) {
        super(value, count);
        this.displayValue = displayValue;
        if (children == null) {
            children = new ArrayList<>();
        }
        this.children = children;
    }

    @NonNull
    @Override
    public String toString() {
        String description = "HierarchicalFacetValue{" + "displayValue='" + displayValue + "', count=" + count;
        if (children.isEmpty()) {
            return description + '}';
        } else {
            return description + ", children=" + children.toString() + '}';
        }
    }
}
