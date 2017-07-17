package com.algolia.instantsearch.events;

import android.support.annotation.NonNull;

import com.algolia.instantsearch.model.NumericRefinement;

public class NumericRefinementEvent extends RefinementEvent {

    public final NumericRefinement refinement;

    public NumericRefinementEvent(Operation operation, @NonNull NumericRefinement refinement) {
        super(refinement.attribute, operation);
        this.refinement = refinement;
    }

    @Override public String toString() {
        return "NumericRefinementEvent{" +
                "attribute='" + attribute + '\'' +
                ", operation=" + operation +
                "refinement=" + refinement +
                '}';
    }
}
