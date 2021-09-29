package com.algolia.instantsearch.helper.searcher.multi

/**
 * A result composed of multiple sub-results.
 */
public interface MultiResult<Result> {

    /**
     * Results list.
     */
    public val results: List<Result>
}
