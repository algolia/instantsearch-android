package com.algolia.instantsearch.model;

@SuppressWarnings("WeakerAccess")
public class FacetStat {
    public final double min;
    public final double max;
    public final double avg;
    public final double sum;

    public FacetStat(double min, double max, double avg, double sum) {
        this.min = min;
        this.max = max;
        this.avg = avg;
        this.sum = sum;
    }
}
