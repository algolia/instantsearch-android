package com.algolia.instantsearch.showcase.compose.hierarchical

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.algolia.instantsearch.compose.hierarchical.HierarchicalState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.hierarchical.HierarchicalConnector
import com.algolia.instantsearch.hierarchical.HierarchicalItem
import com.algolia.instantsearch.hierarchical.HierarchicalPresenterImpl
import com.algolia.instantsearch.hierarchical.connectView
import com.algolia.instantsearch.searcher.connectFilterState
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.compose.*
import com.algolia.instantsearch.showcase.compose.ui.ShowcaseTheme
import com.algolia.instantsearch.showcase.compose.ui.White
import com.algolia.instantsearch.showcase.compose.ui.component.HeaderFilter
import com.algolia.instantsearch.showcase.compose.ui.component.HeaderFilterConnector
import com.algolia.instantsearch.showcase.compose.ui.component.TitleTopBar
import com.algolia.search.model.Attribute

class HierarchicalShowcase : AppCompatActivity() {

    private val hierarchicalCategory = Attribute("hierarchicalCategories")
    private val hierarchicalCategoryLvl0 = Attribute("$hierarchicalCategory.lvl0")
    private val hierarchicalCategoryLvl1 = Attribute("$hierarchicalCategory.lvl1")
    private val hierarchicalCategoryLvl2 = Attribute("$hierarchicalCategory.lvl2")
    private val hierarchicalAttributes = listOf(
        hierarchicalCategoryLvl0,
        hierarchicalCategoryLvl1,
        hierarchicalCategoryLvl2
    )
    private val searcher = HitsSearcher(client, stubIndexName)
    private val filterState = FilterState()
    private val separator = " > "
    private val hierarchicalState = HierarchicalState()
    private val hierarchical = HierarchicalConnector(
        searcher = searcher,
        attribute = hierarchicalCategory,
        filterState = filterState,
        hierarchicalAttributes = hierarchicalAttributes,
        separator = separator
    )

    private val filterHeader = HeaderFilterConnector(
        searcher = searcher,
        filterState = filterState,
        filterColors = filterColors(hierarchicalCategory)
    )

    private val connections = ConnectionHandler(hierarchical)

    init {
        connections += searcher.connectFilterState(filterState)
        connections += hierarchical.connectView(
            hierarchicalState, HierarchicalPresenterImpl(separator)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowcaseTheme {
                HierarchicalScreen()
            }
        }
        configureSearcher(searcher)
        searcher.searchAsync()
    }

    @Composable
    fun HierarchicalScreen(title: String = showcaseTitle) {
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
                        filterHeader = filterHeader,
                    )
                    HierarchicalList(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(White),
                        hierarchicalState = hierarchicalState
                    )
                }
            }
        )
    }

    @Composable
    fun HierarchicalList(modifier: Modifier = Modifier, hierarchicalState: HierarchicalState) {
        LazyColumn(modifier) {
            items(hierarchicalState.hierarchicalItems) { item ->
                Surface(elevation = 1.dp) {
                    HierarchicalItem(
                        modifier = Modifier
                            .height(48.dp)
                            .fillMaxWidth()
                            .padding(start = (item.level * 12).dp),
                        item = item,
                        onClick = { hierarchicalState.onSelectionChanged?.invoke(item.facet.value) }
                    )
                }
            }
        }
    }

    @Composable
    fun HierarchicalItem(
        modifier: Modifier = Modifier,
        item: HierarchicalItem,
        onClick: () -> Unit = {}
    ) {
        Row(
            modifier = modifier
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.displayName,
                style = MaterialTheme.typography.body1,
                fontWeight = if (item.isSelected) FontWeight.Bold else null
            )
            Spacer(modifier = Modifier.weight(1.0f))
            if (item.isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "check",
                    tint = MaterialTheme.colors.primary
                )
            }
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = item.facet.count.toString(),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f)
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connections.clear()
    }
}
