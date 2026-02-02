package com.algolia.instantsearch.examples.android.showcase.compose.ui.component

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.algolia.instantsearch.compose.filter.clear.FilterClear
import com.algolia.instantsearch.compose.item.StatsState
import com.algolia.instantsearch.compose.item.StatsTextState
import com.algolia.instantsearch.core.connection.AbstractConnection
import com.algolia.instantsearch.filter.clear.FilterClearConnector
import com.algolia.instantsearch.filter.clear.connectView
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.filter.state.toFilterGroups
import com.algolia.instantsearch.searcher.SearcherForHits
import com.algolia.instantsearch.stats.StatsConnector
import com.algolia.instantsearch.stats.DefaultStatsPresenter
import com.algolia.instantsearch.stats.connectView
import com.algolia.instantsearch.filter.FilterGroup

class HeaderFilterConnector(
    searcher: SearcherForHits<*>,
    filterState: FilterState,
    val filterColors: Map<String, Color> = emptyMap(),
    val hitsStats: StatsState<String> = StatsTextState(),
    val clearAll: FilterClear = FilterClear(),
) : AbstractConnection() {

    var filterGroups: Set<FilterGroup<*>> by mutableStateOf(emptySet())

    private val filterClear = FilterClearConnector(filterState)
    private val stats = StatsConnector(searcher)

    init {
        filterState.filters.subscribePast {
            filterGroups = it.toFilterGroups()
        }
    }

    private val connections = listOf(
        stats,
        filterClear,
        stats.connectView(hitsStats, DefaultStatsPresenter()),
        filterClear.connectView(clearAll)
    )

    override fun connect() {
        super.connect()
        connections.forEach { it.connect() }
    }

    override fun disconnect() {
        super.disconnect()
        connections.forEach { it.disconnect() }
    }
}
