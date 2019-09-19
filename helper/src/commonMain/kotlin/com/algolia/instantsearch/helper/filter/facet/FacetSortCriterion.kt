package com.algolia.instantsearch.helper.filter.facet


/**
 * A criteria for comparing facets to sort them.
 */
public enum class FacetSortCriterion {
    /**
     * Put currently refined facets first.
     */
    IsRefined,
    /**
     * Put facets with higher count first.
     */
    CountAscending,
    /**
     * Put facets with lower count first.
     */
    CountDescending,

    /**
     * Put facets with higher alphabetical ranking first.
     */
    AlphabeticalAscending,
    /**
     * Put facets with lower alphabetical ranking first.
     */
    AlphabeticalDescending
}