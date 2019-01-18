package com.algolia.instantsearch.core.events;

import com.algolia.instantsearch.core.helpers.Searcher;

import androidx.annotation.NonNull;

/**
 * An event to let you react to refinement of faceted attributes.
 */
public class FacetRefinementEvent extends RefinementEvent {

    /** The refining value. */
    @NonNull public String value;

    /** if {@code true}, this facet is a disjunctive refinement. */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public boolean isDisjunctive;

    public FacetRefinementEvent(final @NonNull Searcher searcher,
                                final @NonNull Operation operation,
                                final @NonNull String attribute,
                                final @NonNull String value,
                                boolean isDisjunctive) {
        super(searcher, operation, attribute);
        this.value = value;
        this.isDisjunctive = isDisjunctive;
    }

    @Override public String toString() {
        return "FacetRefinementEvent{" +
                "searcher=" + searcher +
                ", value='" + value + '\'' +
                ", isDisjunctive=" + isDisjunctive +
                ", attribute='" + attribute + '\'' +
                ", operation=" + operation +
                '}';
    }
}
