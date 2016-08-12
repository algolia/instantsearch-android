package com.algolia.instantsearch.model;

public class FacetValue { //TODO: Rename FacetValue
    final public String value;
    public int count;
    public boolean isEnabled;

    public FacetValue(String value, int count, boolean isEnabled) {
        this.value = value;
        this.count = count;
        this.isEnabled = isEnabled; //FIME: Refactor our inside RefList
    }

    public FacetValue(String value, int count) {
        this(value, count, false);
    }

    @Override
    public String toString() {
        return "FacetValue{" + "value='" + value + ", count=" + count + ", enabled=" + isEnabled + '}';
    }
}
