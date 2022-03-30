package com.algolia.instantsearch.showcase.compose.filter.range

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.algolia.instantsearch.compose.number.range.NumberRangeState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.number.range.Range
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.filter.range.FilterRangeConnector
import com.algolia.instantsearch.filter.range.connectView
import com.algolia.instantsearch.filter.state.FilterGroupID
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.filter.state.filters
import com.algolia.instantsearch.searcher.connectFilterState
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.compose.*
import com.algolia.instantsearch.showcase.compose.R
import com.algolia.instantsearch.showcase.compose.ui.ShowcaseTheme
import com.algolia.instantsearch.showcase.compose.ui.component.HeaderFilter
import com.algolia.instantsearch.showcase.compose.ui.component.HeaderFilterConnector
import com.algolia.instantsearch.showcase.compose.ui.component.RestoreFab
import com.algolia.instantsearch.showcase.compose.ui.component.TitleTopBar
import com.algolia.search.model.Attribute
import java.util.*

class FilterRangeShowcase : AppCompatActivity() {

    private val searcher = HitsSearcher(client, stubIndexName)
    private val price = Attribute("price")
    private val groupID = FilterGroupID(price)
    private val primaryBounds = 0..15
    private val secondaryBounds = 0..10
    private val initialRange = 0..15
    private val filters = filters {
        group(groupID) {
            range(price, initialRange)
        }
    }
    private val filterState = FilterState(filters)

    private val sliderState = NumberRangeState<Int>()
    private val rangeConnector =
        FilterRangeConnector(filterState, price, range = initialRange, bounds = primaryBounds)

    private val filterHeader = HeaderFilterConnector(
        searcher = searcher,
        filterState = filterState,
        filterColors = filterColors(price)
    )

    private val connections = ConnectionHandler(rangeConnector, filterHeader)

    init {
        connections += searcher.connectFilterState(filterState, Debouncer(100))
        connections += rangeConnector.connectView(sliderState)
    }

    private var changeButtonEnabled by mutableStateOf(true)
    private var resetButtonEnabled by mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowcaseTheme {
                FilterRangeScreen(
                    sliderState = sliderState,
                    changeButtonEnabled = changeButtonEnabled,
                    resetButtonEnabled = resetButtonEnabled,
                    onChangeClick = {
                        rangeConnector.viewModel.bounds.value = Range(secondaryBounds)
                        changeButtonEnabled = false
                        resetButtonEnabled = true
                    },
                    onResetClick = {
                        rangeConnector.viewModel.bounds.value = Range(primaryBounds)
                        resetButtonEnabled = false
                        changeButtonEnabled = true
                    }
                )
            }
        }

        configureSearcher(searcher)
        searcher.searchAsync()
    }

    @Composable
    fun FilterRangeScreen(
        title: String = showcaseTitle,
        sliderState: NumberRangeState<Int>,
        onChangeClick: () -> Unit,
        onResetClick: () -> Unit,
        changeButtonEnabled: Boolean,
        resetButtonEnabled: Boolean,
    ) {
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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = rangeText(sliderState.range))
                            Text(text = boundsText(sliderState.bounds))
                        }

                        val range = sliderState.range?.toClosedFloatRange()
                        val bounds = sliderState.bounds?.toClosedFloatRange() ?: 0f..1f
                        var sliderPosition by remember { mutableStateOf(range ?: bounds) }
                        val steps = sliderState.bounds?.let { it.max - it.min + 1 } ?: 0
                        @OptIn(ExperimentalMaterialApi::class)
                        RangeSlider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            steps = steps,
                            values = sliderPosition,
                            onValueChange = { sliderPosition = it },
                            valueRange = bounds,
                            onValueChangeFinished = {
                                sliderState.onRangeChanged?.invoke(sliderPosition.toRange())
                            },
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                enabled = changeButtonEnabled,
                                onClick = onChangeClick
                            ) {
                                Text(stringResource(R.string.bounds_change).uppercase(Locale.getDefault()))
                            }
                            Button(
                                enabled = resetButtonEnabled,
                                onClick = onResetClick,
                                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
                            ) {
                                Text(stringResource(R.string.bounds_reset).uppercase(Locale.getDefault()))
                            }
                        }
                    }
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

