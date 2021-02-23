package com.algolia.instantsearch.core.smartsort

import com.algolia.instantsearch.core.connection.Connection

/**
 * High-level component automatically establishing the connections between smart sort components.
 */
public interface SmartSortConnector : Connection {

    /**
     * The component handling Smart sort logic.
     */
    public val viewModel: SmartSortViewModel
}
