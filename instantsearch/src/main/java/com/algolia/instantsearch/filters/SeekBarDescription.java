package com.algolia.instantsearch.filters;

import com.algolia.instantsearch.model.FacetStat;

class SeekBarDescription extends FilterDescription {
    Double min;
    Double max;
    final int steps;

    SeekBarDescription(String attribute, String name, Double min, Double max, int steps, int position) {
        super(attribute, name, position);
        this.min = min;
        this.max = max;
        this.steps = steps;
    }

    protected void create(FilterResultsWindow window) {
        if (min == null || max == null) {
            FacetStat stats = window.getSearcher().getFacetStat(attribute);
            if (stats == null) {
                throw new RuntimeException("No facet stats were stored for `" + attribute + "`. Did you call addSeekBar(attribute, name, steps) in the activity's onCreate, as required?");
            }
            min = min == null ? stats.min : min;
            max = max == null ? stats.max : max;
        }
        window.createSeekBar(this);
    }

}
