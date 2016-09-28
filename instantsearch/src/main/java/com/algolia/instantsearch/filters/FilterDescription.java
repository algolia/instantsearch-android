package com.algolia.instantsearch.filters;

abstract class FilterDescription {
    protected final String attribute;
    protected final String name;
    protected final int position;

    FilterDescription(String attribute, String name, int position) {
        this.attribute = attribute;
        this.name = name;
        this.position = position;
    }

    protected abstract void create(FilterResultsWindow window);
}
