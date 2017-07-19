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

    public @NonNull final String attribute;
    public @NonNull final Operation operation;

    public RefinementEvent(final @NonNull Operation operation, final @NonNull String attribute) {
        this.attribute = attribute;
        this.operation = operation;
    }

    @Override public String toString() {
        return "RefinementEvent{" +
                "operation=" + operation +
                ", attribute='" + attribute + '\'' +
                '}';
    }
}

