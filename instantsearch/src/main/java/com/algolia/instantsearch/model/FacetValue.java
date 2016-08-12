package com.algolia.instantsearch.model;

import android.support.annotation.NonNull;

public class FacetValue {
    final public String value;
    public int count;

    public FacetValue(String value, int count) {
        this.value = value;
        this.count = count;
    }

    @NonNull @Override
    public String toString() {
        return "FacetValue{" + "value='" + value + ", count=" + count + '}';
    }
}
