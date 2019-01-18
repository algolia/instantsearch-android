package com.algolia.instantsearch.core.events;

import com.algolia.instantsearch.core.helpers.Searcher;

import androidx.annotation.NonNull;

/**
 * An event to let you react to refinement of the search parameters.
 */
@SuppressWarnings("WeakerAccess")
public class RefinementEvent extends SearcherEvent {
    /** An operation applied to the state of refinements. */
    public enum Operation {
        /** A new refinement is applied. */
        ADD,
        /** A current refinement is removed. */
        REMOVE
    }

    /** The attribute that is being refined. */
    @NonNull public final String attribute;
    /** Either {@link Operation#ADD ADD} (adding a new refinement) or {@link Operation#REMOVE REMOVE} (removing a current refinement). */
    @NonNull public final Operation operation;


    public RefinementEvent(@NonNull Searcher searcher,
                           final @NonNull Operation operation,
                           final @NonNull String attribute) {
        super(searcher);
        this.attribute = attribute;
        this.operation = operation;
    }

    @Override public String toString() {
        return "RefinementEvent{" +
                "searcher=" + searcher +
                ", attribute='" + attribute + '\'' +
                ", operation=" + operation +
                '}';
    }
}

