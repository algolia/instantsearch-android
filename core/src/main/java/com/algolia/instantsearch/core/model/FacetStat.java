package com.algolia.instantsearch.core.model;

/**
 * Statistics relative to a given faceted attribute.
 */
@SuppressWarnings("WeakerAccess")
public class FacetStat {
    /** The minimum value for this attribute. */
    public final double min;
    /** The maximum value for this attribute. */
    public final double max;
    /** The average of this attribute's values. */
    public final double avg;
    /** The sum of this attribute's values. */
    public final double sum;

    public FacetStat(double min, double max, double avg, double sum) {
        this.min = min;
        this.max = max;
        this.avg = avg;
        this.sum = sum;
    }
}
