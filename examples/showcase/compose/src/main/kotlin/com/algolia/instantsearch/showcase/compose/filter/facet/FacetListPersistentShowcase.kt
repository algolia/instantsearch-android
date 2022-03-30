package com.algolia.instantsearch.showcase.compose.filter.facet

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.algolia.instantsearch.compose.filter.facet.FacetListState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.filter.facet.FacetListConnector
import com.algolia.instantsearch.filter.facet.connectView
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.searcher.connectFilterState
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.compose.*
import com.algolia.instantsearch.showcase.compose.ui.HoloGreenDark
import com.algolia.instantsearch.showcase.compose.ui.HoloRedDark
import com.algolia.instantsearch.showcase.compose.ui.ShowcaseTheme
import com.algolia.instantsearch.showcase.compose.ui.component.FacetList
import com.algolia.instantsearch.showcase.compose.ui.component.HeaderFilter
import com.algolia.instantsearch.showcase.compose.ui.component.HeaderFilterConnector
import com.algolia.instantsearch.showcase.compose.ui.component.TitleTopBar
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName

class FacetListPersistentShowcase : AppCompatActivity() {

    private val color = Attribute("color")
    private val category = Attribute("category")
    private val index = client.initIndex(IndexName("stub"))
    private val filterState = FilterState()
    private val searcher = HitsSearcher(client, stubIndexName)

    private val facetListStateColor = FacetListState()
    private val facetListColor = FacetListConnector(
        searcher = searcher,
        filterState = filterState,
        attribute = color,
        selectionMode = SelectionMode.Multiple,
        persistentSelection = true
    )

    private val facetListStateCategory = FacetListState()
    private val facetListCategory = FacetListConnector(
        searcher = searcher,
        filterState = filterState,
        attribute = category,
        selectionMode = SelectionMode.Single,
        persistentSelection = true
    )

    private val filterHeader = HeaderFilterConnector(
        searcher = searcher,
        filterState = filterState,
        filterColors = filterColors(color, color, category)
    )

    private val connections = ConnectionHandler(facetListColor, facetListCategory, filterHeader)

    init {
        connections += searcher.connectFilterState(filterState)
        connections += facetListColor.connectView(facetListStateColor)
        connections += facetListCategory.connectView(facetListStateCategory)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowcaseTheme {
                FacetListPersistentScreen()
            }
        }

        configureSearcher(searcher)
        searcher.searchAsync()
    }

    @Composable
    fun FacetListPersistentScreen(title: String = showcaseTitle) {
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
                    Row(Modifier.padding(horizontal = 16.dp)) {
                        FacetList(
                            modifier = Modifier.weight(0.5f),
                            titleColor = HoloRedDark,
                            title = "Multiple Choice",
                            facetListState = facetListStateColor
                        )
                        FacetList(
                            modifier = Modifier.weight(0.5f),
                            titleColor = HoloGreenDark,
                            title = "Since Choice",
                            facetListState = facetListStateCategory
                        )
                    }
                }
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connections.clear()
    }
}
