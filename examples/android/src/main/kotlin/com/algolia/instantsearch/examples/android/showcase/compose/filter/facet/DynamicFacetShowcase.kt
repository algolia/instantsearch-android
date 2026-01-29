package com.algolia.instantsearch.examples.android.showcase.compose.filter.facet

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.algolia.client.api.SearchClient
import com.algolia.client.configuration.ClientOptions
import com.algolia.client.model.search.FacetHits
import com.algolia.instantsearch.compose.filter.facet.dynamic.DynamicFacetListState
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.examples.android.R
import com.algolia.instantsearch.examples.android.showcase.compose.ui.GreyLight
import com.algolia.instantsearch.examples.android.showcase.compose.ui.ShowcaseTheme
import com.algolia.instantsearch.examples.android.showcase.compose.ui.White
import com.algolia.instantsearch.examples.android.showcase.compose.ui.component.FacetRow
import com.algolia.instantsearch.examples.android.showcase.compose.ui.component.HelpDialog
import com.algolia.instantsearch.examples.android.showcase.compose.ui.component.SearchTopBar
import com.algolia.instantsearch.filter.facet.dynamic.AttributedFacets
import com.algolia.instantsearch.filter.facet.dynamic.DynamicFacetListConnector
import com.algolia.instantsearch.filter.facet.dynamic.connectView
import com.algolia.instantsearch.filter.state.FilterOperator
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searchbox.connectView
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import io.ktor.client.plugins.logging.LogLevel

class DynamicFacetShowcase : AppCompatActivity() {

    private val client = SearchClient(
        appId = "RVURKQXRHU",
        apiKey = "937e4e6ec422ff69fe89b569dba30180",
        options = ClientOptions(
            logLevel = LogLevel.ALL,
        )
    )
    private val searcher = HitsSearcher(client, "test_facet_ordering")
    private val filterState = FilterState()
    private val color = "color"
    private val country = "country"
    private val brand = "brand"
    private val size = "size"
    private val dynamicFacetListState = DynamicFacetListState()
    private val dynamicFacets = DynamicFacetListConnector(
        searcher = searcher,
        filterState = filterState,
        selectionModeForAttribute = mapOf(
            color to SelectionMode.Multiple,
            country to SelectionMode.Multiple
        ),
        filterGroupForAttribute = mapOf(
            brand to (brand to FilterOperator.Or),
            color to (color to FilterOperator.Or),
            size to (color to FilterOperator.Or),
            country to (color to FilterOperator.Or),
        )
    )

    private val searchBoxState = SearchBoxState()
    private val searchBox = SearchBoxConnector(searcher)

    private val connections = ConnectionHandler(dynamicFacets, searchBox)

    init {
        connections += dynamicFacets.connectView(dynamicFacetListState)
        connections += searchBox.connectView(searchBoxState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowcaseTheme {
                DynamicFacetScreen(searchBoxState, dynamicFacetListState)
            }
        }
        searcher.query = searcher.query.copy(facets = listOf(brand, color, size, country))
        searcher.searchAsync()
    }

    @Composable
    fun DynamicFacetScreen(
        searchBoxState: SearchBoxState,
        dynamicFacetListState: DynamicFacetListState
    ) {
        val openDialog = remember { mutableStateOf(false) }
        Scaffold(
            topBar = {
                SearchTopBar(
                    searchBoxState = searchBoxState,
                    icon = Icons.Default.Info,
                    onIconClick = { openDialog.value = true }
                )
            },
            content = { padding ->
                Column(
                    Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(padding)) {
                    dynamicFacetListState.orderedFacets.onEach { attributedFacet ->
                        OrderedFacets(Modifier.padding(8.dp), attributedFacet) { facet, attribute ->
                            dynamicFacetListState.toggle(facet, attribute)
                        }
                    }
                }
            }
        )
        HelpDialog(openDialog, stringResource(R.string.dynamic_facet_help))
    }

    @Composable
    fun OrderedFacets(
        modifier: Modifier = Modifier,
        attributedFacet: AttributedFacets,
        onClick: (FacetHits, String) -> Unit
    ) {
        Text(
            modifier = modifier,
            text = attributedFacet.attribute,
            style = MaterialTheme.typography.subtitle2,
            color = GreyLight,
        )
        FacetItems(attributedFacet, onClick)
    }

    @Composable
    fun FacetItems(
        attributedFacet: AttributedFacets,
        onClick: (FacetHits, String) -> Unit
    ) {
        Surface(
            contentColor = White,
            elevation = 1.dp,
        ) {
            Column {
                val (attribute, facets) = attributedFacet
                facets.forEach { facet ->
                    val selected = dynamicFacetListState.isSelected(facet, attribute)
                    FacetRow(
                        modifier = Modifier.fillMaxWidth(),
                        selectableFacet = facet to selected,
                        onClick = { onClick(facet, attribute) }
                    )
                    if (facet != facets.lastOrNull()) Divider()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connections.clear()
    }
}
