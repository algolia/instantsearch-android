package com.algolia.instantsearch.model;

public class Facet {
    final public String value;
    public int count;
    public boolean isEnabled;

    public Facet(String value, int count, boolean isEnabled) {
        this.value = value;
        this.count = count;
        this.isEnabled = isEnabled;
    }

    public Facet(String value, int count) {
        this(value, count, false);
    }

    @Override
    public String toString() {
        return "Facet{" + "value='" + value + ", count=" + count + ", enabled=" + isEnabled + '}';
    }
}
