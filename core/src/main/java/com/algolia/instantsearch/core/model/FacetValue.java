package com.algolia.instantsearch.core.model;

import androidx.annotation.NonNull;

/**
 * Describes a faceted attribute's value and the associated count.
 */
public class FacetValue {
    /** A value for a faceted attribute. */
    @NonNull final public String value;
    /** The count of records matching this facet value. */
    public int count;

    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public FacetValue(@NonNull String value, int count) {
        this.value = value;
        this.count = count;
    }

    @NonNull
    @Override
    public String toString() {
        return "FacetValue{" + "value='" + value + ", count=" + count + '}';
    }
}
