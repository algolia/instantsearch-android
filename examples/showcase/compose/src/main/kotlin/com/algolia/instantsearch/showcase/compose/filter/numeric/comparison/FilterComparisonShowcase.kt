package com.algolia.instantsearch.showcase.compose.filter.numeric.comparison

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.algolia.instantsearch.compose.number.NumberState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.number.decrement
import com.algolia.instantsearch.core.number.increment
import com.algolia.instantsearch.core.number.just
import com.algolia.instantsearch.filter.numeric.comparison.FilterComparisonConnector
import com.algolia.instantsearch.filter.numeric.comparison.connectView
import com.algolia.instantsearch.filter.numeric.comparison.setBoundsFromFacetStatsInt
import com.algolia.instantsearch.filter.numeric.comparison.setBoundsFromFacetStatsLong
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.searcher.addFacet
import com.algolia.instantsearch.searcher.connectFilterState
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.compose.*
import com.algolia.instantsearch.showcase.compose.R
import com.algolia.instantsearch.showcase.compose.ui.ShowcaseTheme
import com.algolia.instantsearch.showcase.compose.ui.component.HeaderFilter
import com.algolia.instantsearch.showcase.compose.ui.component.HeaderFilterConnector
import com.algolia.instantsearch.showcase.compose.ui.component.TitleTopBar
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.NumericOperator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FilterComparisonShowcase : AppCompatActivity() {

    private val price = Attribute("price")
    private val year = Attribute("year")
    private val filterState = FilterState()
    private val searcher = HitsSearcher(client, stubIndexName)

    private val comparisonPriceState = NumberState<Long>("-")
    private val comparisonPrice =
        FilterComparisonConnector<Long>(filterState, price, NumericOperator.GreaterOrEquals)

    private val comparisonYearState = NumberState<Int>()
    private val comparisonYear =
        FilterComparisonConnector<Int>(filterState, year, NumericOperator.Equals)

    private val filterHeader = HeaderFilterConnector(
        searcher = searcher,
        filterState = filterState,
        filterColors = filterColors(price, year)
    )

    private val hintState = mutableStateOf("")
    private val yearLabel = "$year ${comparisonYear.operator.raw}"
    private val priceLabel = "$price ${comparisonPrice.operator.raw}"

    private val connection = ConnectionHandler(comparisonPrice, comparisonYear, filterHeader)

    init {
        connection += searcher.connectFilterState(filterState)
        connection += comparisonPrice.connectView(comparisonPriceState)
        connection += comparisonYear.connectView(comparisonYearState) { year ->
            year?.toString() ?: ""
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowcaseTheme {
                FilterComparisonScreen()
            }
        }
        searcher.query.addFacet(price)
        searcher.query.addFacet(year)

        configureSearcher(searcher)

        searcher.coroutineScope.launch {
            val response = searcher.search()
            response.facetStatsOrNull?.let {
                comparisonPrice.viewModel.setBoundsFromFacetStatsLong(price, it)
                comparisonYear.viewModel.setBoundsFromFacetStatsInt(year, it)
                withContext(Dispatchers.Main) {
                    val bounds = comparisonYear.viewModel.bounds.value!!
                    hintState.value = getString(R.string.year_range, bounds.min, bounds.max)
                }
            }
        }
    }

    @Composable
    fun FilterComparisonScreen(title: String = showcaseTitle) {
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
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PriceInput(
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .weight(0.5f),
                            comparisonState = comparisonPriceState,
                            priceLabel = priceLabel
                        )
                        YearInput(
                            modifier = Modifier
                                .padding(top = 21.dp)
                                .weight(0.5f),
                            comparisonState = comparisonYearState,
                            yearLabel = yearLabel,
                            hint = hintState.value
                        )
                    }
                }
            }
        )
    }

    @Composable
    fun PriceInput(
        modifier: Modifier = Modifier,
        comparisonState: NumberState<Long>,
        priceLabel: String,
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = priceLabel,
                style = MaterialTheme.typography.body1
            )
            Column(
                modifier = Modifier.padding(start = 8.dp, end = 16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowDropUp,
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .clickable {
                            comparisonState.computation.increment()
                        }
                )
                Text(
                    modifier = Modifier.width(36.dp),
                    text = comparisonState.text,
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .clickable {
                            comparisonState.computation.decrement()
                        }
                )
            }
        }
    }

    @Composable
    fun YearInput(
        modifier: Modifier = Modifier,
        comparisonState: NumberState<Int>,
        yearLabel: String,
        hint: String
    ) {
        Column(modifier) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = yearLabel,
                    style = MaterialTheme.typography.body1
                )
                OutlinedTextField(
                    modifier = Modifier.padding(start = 8.dp, end = 16.dp),
                    value = comparisonState.text,
                    onValueChange = { comparisonState.setText(it) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            val yearInt = comparisonState.text.toIntOrNull()
                            comparisonState.computation.just(yearInt)
                        }
                    )
                )
            }
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = hint,
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}