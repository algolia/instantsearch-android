package com.algolia.instantsearch.showcase.compose.filter.current

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.algolia.instantsearch.compose.filter.current.FilterCurrentState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.filter.current.FilterCurrentConnector
import com.algolia.instantsearch.filter.current.connectView
import com.algolia.instantsearch.filter.state.FilterGroupID
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.filter.state.filters
import com.algolia.instantsearch.searcher.connectFilterState
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.compose.*
import com.algolia.instantsearch.showcase.compose.filter.current.ui.FilterChips
import com.algolia.instantsearch.showcase.compose.ui.ShowcaseTheme
import com.algolia.instantsearch.showcase.compose.ui.component.HeaderFilter
import com.algolia.instantsearch.showcase.compose.ui.component.HeaderFilterConnector
import com.algolia.instantsearch.showcase.compose.ui.component.RestoreFab
import com.algolia.instantsearch.showcase.compose.ui.component.TitleTopBar
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.NumericOperator

class FilterCurrentShowcase : AppCompatActivity() {

    private val color = Attribute("color")
    private val price = Attribute("price")
    private val tags = Attribute("_tags")
    private val groupColor = FilterGroupID(color)
    private val groupPrice = FilterGroupID(price)
    private val groupTags = FilterGroupID(tags)
    private val filters = filters {
        group(groupColor) {
            facet(color, "red")
            facet(color, "green")
        }
        group(groupTags) {
            tag("mobile")
        }
        group(groupPrice) {
            comparison(price, NumericOperator.NotEquals, 42)
            range(price, IntRange(0, 100))
        }
    }
    private val filterState = FilterState(filters)
    private val searcher = HitsSearcher(client, stubIndexName)
    private val currentFiltersAll = FilterCurrentConnector(filterState)
    private val currentFiltersColor = FilterCurrentConnector(filterState, listOf(groupColor))

    private val chipGroupAll = FilterCurrentState()
    private val chipGroupColors = FilterCurrentState()

    private val filterHeader = HeaderFilterConnector(
        searcher = searcher,
        filterState = filterState,
        filterColors = filterColors(color, price, tags)
    )

    private val connections = ConnectionHandler(
        currentFiltersAll, currentFiltersColor, filterHeader
    )

    init {
        connections += searcher.connectFilterState(filterState)
        connections += currentFiltersAll.connectView(chipGroupAll)
        connections += currentFiltersColor.connectView(chipGroupColors)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowcaseTheme {
                FilterCurrentScreen()
            }
        }

        configureSearcher(searcher)
        searcher.searchAsync()
    }

    @Composable
    fun FilterCurrentScreen(title: String = showcaseTitle) {
        Scaffold(
            topBar = {
                TitleTopBar(
                    title = title,
                    onBackPressed = ::onBackPressed
                )
            },
            content = {
                Column(Modifier.fillMaxWidth()) {
                    HeaderFilter(
                        modifier = Modifier.padding(16.dp),
                        filterHeader = filterHeader
                    )
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = "Current filters",
                        style = MaterialTheme.typography.caption
                    )
                    FilterChips(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        filterCurrentState = chipGroupAll,
                        rows = 2
                    )
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = "Current colors filters",
                        style = MaterialTheme.typography.caption
                    )
                    FilterChips(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        filterCurrentState = chipGroupColors,
                        rows = 1
                    )
                }
            },
            floatingActionButton = {
                RestoreFab { filterState.notify { set(filters) } }
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connections.clear()
    }
}
