package com.algolia.instantsearch.examples.android.showcase.compose.filter.map

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.algolia.instantsearch.compose.filter.map.FilterMapState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.examples.android.showcase.compose.client
import com.algolia.instantsearch.examples.android.showcase.compose.configureSearcher
import com.algolia.instantsearch.examples.android.showcase.compose.filterColors
import com.algolia.instantsearch.examples.android.showcase.compose.showcaseTitle
import com.algolia.instantsearch.examples.android.showcase.compose.stubIndexName
import com.algolia.instantsearch.examples.android.showcase.compose.ui.ShowcaseTheme
import com.algolia.instantsearch.examples.android.showcase.compose.ui.component.HeaderFilter
import com.algolia.instantsearch.examples.android.showcase.compose.ui.component.HeaderFilterConnector
import com.algolia.instantsearch.examples.android.showcase.compose.ui.component.TitleTopBar
import com.algolia.instantsearch.filter.map.FilterMapConnector
import com.algolia.instantsearch.filter.map.connectView
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.filter.state.groupAnd
import com.algolia.instantsearch.searcher.connectFilterState
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter

class FilterMapShowcase : AppCompatActivity() {

    private val gender = Attribute("gender")
    private val groupGender = groupAnd(gender)
    private val filterState = FilterState()
    private val searcher = HitsSearcher(client, stubIndexName)
    private val filters = mapOf(
        0 to Filter.Facet(gender, "male"),
        1 to Filter.Facet(gender, "female")
    )

    private val filterMapState = FilterMapState()
    private val filterMap = FilterMapConnector(filters, filterState, groupID = groupGender)

    private val filterHeader = HeaderFilterConnector(
        searcher = searcher,
        filterState = filterState,
        filterColors = filterColors(gender)
    )

    private val connection = ConnectionHandler(filterMap, filterHeader)

    init {
        connection += searcher.connectFilterState(filterState)
        connection += filterMap.connectView(filterMapState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowcaseTheme {
                FilterMapScreen()
            }
        }
        configureSearcher(searcher)
        searcher.searchAsync()
    }

    @Composable
    fun FilterMapScreen(title: String = showcaseTitle) {
        Scaffold(
            topBar = { TitleTopBar(title = title) },
            content = { padding ->
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(padding)
                ) {
                    HeaderFilter(
                        modifier = Modifier.padding(16.dp),
                        filterHeader = filterHeader
                    )
                    Column(Modifier.padding(horizontal = 16.dp)) {
                        filterMapState.options.onEach { (index, facet) ->
                            Option(
                                modifier = Modifier.padding(vertical = 8.dp),
                                text = facet,
                                selected = index == filterMapState.selected,
                                onClick = { filterMapState.selectOption(index) }
                            )
                        }
                    }
                }
            }
        )
    }

    @Composable
    fun Option(
        modifier: Modifier = Modifier,
        text: String,
        selected: Boolean,
        onClick: () -> Unit = {}
    ) {
        Row(modifier) {
            RadioButton(
                selected = selected,
                colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colors.primary),
                onClick = onClick
            )
            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = text,
                style = MaterialTheme.typography.body2,
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}
