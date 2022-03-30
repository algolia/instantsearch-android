package com.algolia.instantsearch.showcase.compose.filter.rating

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.algolia.instantsearch.showcase.compose.client
import com.algolia.instantsearch.showcase.compose.filterColors
import com.algolia.instantsearch.showcase.compose.showcaseTitle
import com.algolia.instantsearch.showcase.compose.ui.ShowcaseTheme
import com.algolia.instantsearch.showcase.compose.ui.component.HeaderFilter
import com.algolia.instantsearch.showcase.compose.ui.component.HeaderFilterConnector
import com.algolia.instantsearch.showcase.compose.ui.component.RatingBar
import com.algolia.instantsearch.showcase.compose.ui.component.TitleTopBar
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.Filter

class RatingShowcase : AppCompatActivity() {

    private val searcher = HitsSearcher(client, IndexName("instant_search"))
    private val rating = Attribute("rating")
    private val groupID = FilterGroupID(rating)
    private val primaryBounds = 0f..5f
    private val initialRange = 3f..5f
    private val filters = filters {
        group(groupID) {
            +Filter.Numeric(
                rating,
                lowerBound = initialRange.start,
                upperBound = initialRange.endInclusive
            )
        }
    }
    private val filterState = FilterState(filters)

    private val ratingState = NumberRangeState<Float>()
    private val range =
        FilterRangeConnector(filterState, rating, range = initialRange, bounds = primaryBounds)

    private val filterHeader = HeaderFilterConnector(
        searcher = searcher,
        filterState = filterState,
        filterColors = filterColors(rating)
    )

    private val connections = ConnectionHandler(range, filterHeader)

    init {
        connections += searcher.connectFilterState(filterState, Debouncer(100))
        connections += range.connectView(ratingState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowcaseTheme {
                RatingScreen()
            }
        }
        searcher.searchAsync()
    }

    @Composable
    fun RatingScreen(title: String = showcaseTitle) {
        ResetInitialRangeIfNeeded()

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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        val step = 0.5f

                        RatingBar(
                            ratingState = ratingState,
                            step = step
                        )
                        Text(
                            modifier = Modifier.padding(horizontal = 6.dp),
                            text = "${ratingState.range?.min ?: 0}",
                            style = MaterialTheme.typography.body2,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.primary
                        )
                        PlusMinusButton(
                            onPlusClick = {
                                ratingState.range?.let {
                                    ratingState.onRangeChanged?.invoke(
                                        Range((it.min + step).coerceAtMost(it.max), it.max)
                                    )
                                }
                            },
                            onMinusClick = {
                                ratingState.range?.let {
                                    ratingState.onRangeChanged?.invoke(
                                        Range((it.min - step).coerceAtLeast(0f), it.max)
                                    )
                                }
                            }
                        )
                    }
                }
            }
        )
    }

    @Composable
    fun PlusMinusButton(
        modifier: Modifier = Modifier,
        onPlusClick: () -> Unit,
        onMinusClick: () -> Unit,
    ) {
        Row(modifier) {
            OutlinedButton(
                shape = MaterialTheme.shapes.large,
                onClick = onMinusClick,
            ) {
                Icon(imageVector = Icons.Default.Remove, contentDescription = null)
            }
            OutlinedButton(
                shape = MaterialTheme.shapes.large,
                onClick = onPlusClick
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    }

    @Composable
    private fun ResetInitialRangeIfNeeded() {
        if (ratingState.range == null) {
            ratingState.setRange(Range(initialRange))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connections.clear()
    }
}