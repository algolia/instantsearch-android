package com.algolia.instantsearch.showcase.compose.filter.toggle

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.algolia.instantsearch.compose.filter.toggle.FilterToggleState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.filter.toggle.FilterToggleConnector
import com.algolia.instantsearch.filter.toggle.connectView
import com.algolia.instantsearch.searcher.connectFilterState
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.compose.*
import com.algolia.instantsearch.showcase.compose.ui.ShowcaseTheme
import com.algolia.instantsearch.showcase.compose.ui.component.HeaderFilter
import com.algolia.instantsearch.showcase.compose.ui.component.HeaderFilterConnector
import com.algolia.instantsearch.showcase.compose.ui.component.TitleTopBar
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator

class FilterToggleShowcase : AppCompatActivity() {

    private val promotions = Attribute("promotions")
    private val size = Attribute("size")
    private val tags = Attribute("_tags")
    private val filterState = FilterState()
    private val searcher = HitsSearcher(client, stubIndexName)

    private val couponState = FilterToggleState()
    private val filterCoupon = Filter.Facet(promotions, "coupon")
    private val toggleCoupon = FilterToggleConnector(filterState, filterCoupon)

    private val sizeState = FilterToggleState()
    private val filterSize = Filter.Numeric(size, NumericOperator.Greater, 40)
    private val toggleSize = FilterToggleConnector(filterState, filterSize)

    private val vintageState = FilterToggleState()
    private val filterVintage = Filter.Tag("vintage")
    private val toggleVintage = FilterToggleConnector(filterState, filterVintage)

    private val filterHeader = HeaderFilterConnector(
        searcher = searcher,
        filterState = filterState,
        filterColors = filterColors(size, tags)
    )

    private val connections = ConnectionHandler(
        toggleCoupon, toggleSize, toggleVintage, filterHeader
    )

    init {
        connections += searcher.connectFilterState(filterState)
        connections += toggleCoupon.connectView(couponState)
        connections += toggleSize.connectView(sizeState)
        connections += toggleVintage.connectView(vintageState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowcaseTheme {
                FilterToggleScreen()
            }
        }
        configureSearcher(searcher)
        searcher.searchAsync()
    }

    @Composable
    private fun FilterToggleScreen(title: String = showcaseTitle) {
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
                    ) {
                        Column(Modifier.weight(0.5f)) {
                            LabeledCheckBox(
                                filterToggleState = sizeState
                            )
                            LabeledCheckBox(
                                modifier = Modifier.padding(top = 24.dp),
                                filterToggleState = vintageState
                            )
                        }

                        Column(Modifier.weight(0.5f)) {
                            LabeledSwitch(filterToggleState = couponState)
                        }
                    }
                }
            }
        )
    }

    @Composable
    fun LabeledCheckBox(modifier: Modifier = Modifier, filterToggleState: FilterToggleState) {
        Row(modifier) {
            Checkbox(
                checked = filterToggleState.isSelected,
                onCheckedChange = filterToggleState::changeSelection,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colors.primary
                )
            )
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = filterToggleState.item,
                style = MaterialTheme.typography.body1
            )
        }
    }

    @Composable
    fun LabeledSwitch(modifier: Modifier = Modifier, filterToggleState: FilterToggleState) {
        Row(modifier) {
            Switch(
                checked = filterToggleState.isSelected,
                onCheckedChange = filterToggleState::changeSelection,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colors.primary
                )
            )
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = filterToggleState.item,
                style = MaterialTheme.typography.body1
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connections.clear()
    }
}


