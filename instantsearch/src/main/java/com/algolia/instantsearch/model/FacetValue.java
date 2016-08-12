package com.algolia.instantsearch.model;

public class FacetValue {
    final public String value;
    public int count;

    public FacetValue(String value, int count) {
        this.value = value;
        this.count = count;
    }

    @Override
    public String toString() {
        return "FacetValue{" + "value='" + value + ", count=" + count + '}';
    }
}
