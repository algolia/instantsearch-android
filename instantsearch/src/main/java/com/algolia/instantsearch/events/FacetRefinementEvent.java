package com.algolia.instantsearch.events;

import android.support.annotation.NonNull;

public class FacetRefinementEvent extends RefinementEvent {

    /** The refining value. */
    public @NonNull String value;
    public boolean isDisjunctive;

    public FacetRefinementEvent(final @NonNull Operation operation, final @NonNull String attribute, final @NonNull String value, boolean isDisjunctive) {
        super(operation, attribute);
        this.value = value;
        this.isDisjunctive = isDisjunctive;
    }

    @Override public String toString() {
        return "FacetRefinementEvent{" +
                "operation=" + operation +
                ", attribute='" + attribute + '\'' +
                ", value=" + value + '\'' +
                ", isDisjunctive=" + isDisjunctive +
                '}';
    }
}
