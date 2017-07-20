package com.algolia.instantsearch.events;

import android.support.annotation.NonNull;

/**
 * An event to let you react to refinement of the search parameters.
 */
@SuppressWarnings("WeakerAccess")
public class RefinementEvent {
    /** An operation applied to the state of refinements. */
    public enum Operation {
        /** A new refinement is applied. */
        ADD,
        /** A current refinement is removed. */
        REMOVE
    }

    /** The attribute that is being refined. */
    public @NonNull final String attribute;
    /** Either {@link Operation#ADD ADD} (adding a new refinement) or {@link Operation#REMOVE REMOVE} (removing a current refinement). */
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

