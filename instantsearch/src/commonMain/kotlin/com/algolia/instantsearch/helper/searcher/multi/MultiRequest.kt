package com.algolia.instantsearch.helper.searcher.multi

/**
 * A request composed of multiple sub-requests.
 */
public interface MultiRequest<Request> {

    /**
     * Requests list.
     */
    public val requests: List<Request>
}
