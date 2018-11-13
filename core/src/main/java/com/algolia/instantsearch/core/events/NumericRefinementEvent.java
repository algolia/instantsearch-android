package com.algolia.instantsearch.core.events;

import com.algolia.instantsearch.core.helpers.Searcher;
import com.algolia.instantsearch.core.model.NumericRefinement;

import androidx.annotation.NonNull;

/**
 * An event to let you react to refinement of numerical attributes.
 */
public class NumericRefinementEvent extends RefinementEvent {

    /** A description of the refinement: {@link NumericRefinement#attribute attribute}
     * is refined with {@link NumericRefinement#value value}
     * using {@link NumericRefinement#operator operator}. */
    @NonNull public final NumericRefinement refinement;

    public NumericRefinementEvent(final @NonNull Searcher searcher,
                                  final @NonNull Operation operation,
                                  final @NonNull NumericRefinement refinement) {
        super(searcher, operation, refinement.attribute);
        this.refinement = refinement;
    }

    @Override public String toString() {
        return "NumericRefinementEvent{" +
                "operation=" + operation +
                ", attribute='" + attribute + '\'' +
                ", refinement=" + refinement +
                '}';
    }
}
