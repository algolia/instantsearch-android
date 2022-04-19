package com.algolia.instantsearch.stats

import com.algolia.instantsearch.core.connection.AbstractConnection
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.extension.traceStatsConnector
import com.algolia.search.model.response.ResponseSearch

/**
 * Each search `Response` contains various metadata that you might display in your search experience.
 * The following information is available as a part of the `Response`:
 * - `hitsPerPage`: Number of hits per page.
 * - `totalHitsCount`: Total number of hits.
 * - `pagesCount`: Total number of pages.
 * - `page`: Current page.
 * - `processingTimeMS`: Processing time of the request (in ms).
 * - `query`: Query text that produced these results.
 * [Documentation](https://www.algolia.com/doc/api-reference/widgets/stats/android/)
 *
 * @param searcher the Searcher that handles your searches
 * @param viewModel the logic applied to the stats
 */
public data class StatsConnector(
    public val searcher: Searcher<ResponseSearch>,
    public val viewModel: StatsViewModel = StatsViewModel(),
) : AbstractConnection() {

    /**
     * @param searcher the Searcher that handles your searches
     * @param responseSearch the initial search response
     */
    public constructor(
        searcher: Searcher<ResponseSearch>,
        responseSearch: ResponseSearch,
    ) : this(searcher, StatsViewModel(responseSearch))

    private val connectionSearcher = viewModel.connectSearcher(searcher)

    init {
        traceStatsConnector()
    }

    override fun connect() {
        super.connect()
        connectionSearcher.connect()
    }

    override fun disconnect() {
        super.disconnect()
        connectionSearcher.disconnect()
    }
}
