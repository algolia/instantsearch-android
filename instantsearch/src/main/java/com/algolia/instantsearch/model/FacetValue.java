package com.algolia.instantsearch.model;

import android.support.annotation.NonNull;

/**
 * A facet value and the associated count.
 */
public class FacetValue {
    /** A value for a faceted attribute. */
    final public String value;
    /** The count of records matching this facet value. */
    public int count;

    public FacetValue(String value, int count) {
        this.value = value;
        this.count = count;
    }

    @NonNull
    @Override
    public String toString() {
        return "FacetValue{" + "value='" + value + ", count=" + count + '}';
    }
}
