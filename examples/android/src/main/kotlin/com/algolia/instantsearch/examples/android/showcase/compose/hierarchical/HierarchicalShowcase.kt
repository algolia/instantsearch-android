package com.algolia.instantsearch.examples.android.showcase.compose.hierarchical

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.algolia.instantsearch.compose.hierarchical.HierarchicalState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.tree.Node
import com.algolia.instantsearch.examples.android.showcase.compose.filterColors
import com.algolia.instantsearch.examples.android.showcase.compose.showcaseTitle
import com.algolia.instantsearch.examples.android.showcase.compose.ui.ShowcaseTheme
import com.algolia.instantsearch.examples.android.showcase.compose.ui.White
import com.algolia.instantsearch.examples.android.showcase.compose.ui.component.HeaderFilter
import com.algolia.instantsearch.examples.android.showcase.compose.ui.component.HeaderFilterConnector
import com.algolia.instantsearch.examples.android.showcase.compose.ui.component.TitleTopBar
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.hierarchical.DefaultHierarchicalPresenter
import com.algolia.instantsearch.hierarchical.HierarchicalConnector
import com.algolia.instantsearch.hierarchical.HierarchicalItem
import com.algolia.instantsearch.hierarchical.HierarchicalPresenter
import com.algolia.instantsearch.hierarchical.HierarchicalTree
import com.algolia.instantsearch.hierarchical.connectView
import com.algolia.instantsearch.searcher.connectFilterState
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.search.client.ClientSearch
import com.algolia.search.configuration.ConfigurationSearch
import com.algolia.search.logging.LogLevel
import com.algolia.search.logging.Logger
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.search.Query

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

    val languageTradeInnCode = "eng"
    val hierarchicalCategoriesAttribute = Attribute("hierarchicalCategories")
    val hierarchicalCategoriesAttributeLvl1 = Attribute("$hierarchicalCategoriesAttribute.${languageTradeInnCode}.lvl1")
    val hierarchicalCategoriesAttributeLvl2 = Attribute("$hierarchicalCategoriesAttribute.${languageTradeInnCode}.lvl2")
    val hierarchicalCategoriesAttributes = listOf(
        hierarchicalCategoriesAttributeLvl1,
        hierarchicalCategoriesAttributeLvl2
    )

    private val client = ClientSearch(
        configuration = ConfigurationSearch(
            applicationID = ApplicationID("N6VYUYLE6W"),
            apiKey = APIKey("3d27121f3a7b3f34890f408160aa0f04"),
            logLevel = LogLevel.All,
            logger = Logger.messageLengthLimiting()
        )
    )

    private val searcher = HitsSearcher(
        client = client,
        query = Query(
            attributesToRetrieve = listOf(Attribute("objectID")),
            attributesToHighlight = listOf(Attribute("objectID"))
        ),
        indexName = IndexName("products_search"),
    )
    private val filterState = FilterState()
    private val separator = " > "
    private val hierarchicalState = HierarchicalState()
    private val hierarchical = HierarchicalConnector(
        searcher = searcher,
        attribute = hierarchicalCategory,
        filterState = filterState,
        hierarchicalAttributes = hierarchicalCategoriesAttributes,
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
            hierarchicalState, DefaultHierarchicalPresenter(separator)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowcaseTheme {
                HierarchicalScreen()
            }
        }
        //configureSearcher(searcher)
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

public class MyHierarchicalPresenter(
    public val separator: String,
    public val comparator: Comparator<HierarchicalItem> = Comparator { a, b -> a.facet.value.compareTo(b.facet.value) },
) : HierarchicalPresenter<List<HierarchicalItem>> {

    override fun invoke(tree: HierarchicalTree): List<HierarchicalItem> {
        //children.asTree(0, transform).sortedWith(comparator)

        return tree.children.asTree(1) { node, level, _ ->
            HierarchicalItem(
                facet = node.content,
                displayName = node.content.value.split(separator)[level],
                level = level,
                isSelected = node.isSelected
            )
        }.sortedWith(comparator)
    }

    private fun <I, O> List<Node<I>>.asTree(
        level: Int = 0,
        transform: (Node<I>, Int, Boolean) -> O
    ): List<O> {
        return mutableListOf<O>().also { list ->
            forEach { node ->
                list += transform(node, level, node.children.isEmpty())
                list += node.children.asTree(level + 1, transform)
            }
        }
    }
}
