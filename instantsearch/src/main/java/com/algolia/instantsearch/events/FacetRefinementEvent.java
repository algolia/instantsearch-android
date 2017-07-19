package com.algolia.instantsearch.events;

import android.support.annotation.NonNull;

public class FacetRefinementEvent extends RefinementEvent {

    public @NonNull String value;

    public FacetRefinementEvent(final @NonNull Operation operation, final @NonNull String attribute, final @NonNull String value) {
        super(operation, attribute);
        this.value = value;
    }

    @Override public String toString() {
        return "FacetRefinementEvent{" +
                "operation=" + operation +
                ", attribute='" + attribute + '\'' +
                ", value=" + value +
                '}';
    }
}
