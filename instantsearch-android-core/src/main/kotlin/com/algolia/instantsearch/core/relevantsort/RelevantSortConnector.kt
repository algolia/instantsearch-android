package com.algolia.instantsearch.core.relevantsort

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.searcher.Searcher

/**
 * High-level component automatically establishing the connections between relevant sort components.
 */
public interface RelevantSortConnector : Connection {

    /**
     * The component handling relevant sort logic.
     */
    public val viewModel: RelevantSortViewModel

    /**
     * Searcher that handles your searches.
     */
    public val searcher: Searcher<*>
}
