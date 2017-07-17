package com.algolia.instantsearch.events;

import android.support.annotation.NonNull;

/**
 * An event to let you react to refinement of the search parameters.
 */
@SuppressWarnings("WeakerAccess")
public class RefinementEvent {
    public enum Operation {
        ADD,
        REMOVE
    }

    public final String attribute;
    public Operation operation;

    public RefinementEvent(final @NonNull String attribute, Operation operation) {
        this.attribute = attribute;
        this.operation = operation;
    }

    @Override public String toString() {
        return "RefinementEvent{" +
                "attribute='" + attribute + '\'' +
                ", operation=" + operation +
                '}';
    }
}

