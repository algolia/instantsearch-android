package com.algolia.instantsearch.showcase.compose.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.algolia.instantsearch.compose.filter.list.FilterListState
import com.algolia.instantsearch.core.selectable.list.SelectableItem
import com.algolia.instantsearch.filter.FilterPresenter
import com.algolia.instantsearch.filter.FilterPresenterImpl
import com.algolia.instantsearch.showcase.compose.ui.BlueDark
import com.algolia.instantsearch.showcase.compose.ui.ShowcaseTheme
import com.algolia.instantsearch.showcase.compose.ui.White
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator

@Preview
@Composable
fun FilterListPreview() {
    val price = Attribute("price")
    val color = Attribute("color")
    ShowcaseTheme {
        FilterList(
            filterListState = FilterListState(
                listOf(
                    //(Numeric(attribute=price, isNegated=false, value=Range(lowerBound=5, upperBound=10)), false)
                    Filter.Numeric(attribute = price, range = 5..10) to true,
                    Filter.Tag(value = "coupon") to false,
                    Filter.Facet(attribute = color, value = "red") to false,
                    Filter.Facet(attribute = color, value = "red") to false,
                    Filter.Numeric(attribute = price, NumericOperator.Greater, 100) to false,
                )
            )
        )
    }
}

@Composable
fun <T : Filter> FilterList(
    modifier: Modifier = Modifier,
    presenter: FilterPresenter = FilterPresenterImpl(),
    filterListState: FilterListState<T>
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            contentColor = White,
            modifier = Modifier.padding(horizontal = 4.dp),
            elevation = 1.dp,
        ) {
            Column {
                filterListState.items.forEachIndexed { index, selectableFacet ->
                    FilterRow(
                        modifier = Modifier.fillMaxWidth(),
                        selectableFilter = selectableFacet,
                        presenter = presenter,
                        onClick = { filterListState.onSelection?.invoke(it) }
                    )
                    if (index != filterListState.items.lastIndex) Divider()
                }
            }
        }
    }
}

@Composable
fun <T : Filter> FilterRow(
    modifier: Modifier = Modifier,
    selectableFilter: SelectableItem<T>,
    presenter: FilterPresenter = FilterPresenterImpl(),
    onClick: (T) -> Unit = {}
) {
    val (filter, isSelected) = selectableFilter
    Row(
        modifier = modifier
            .height(50.dp)
            .clickable(onClick = { onClick(filter) })
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = presenter(filter),
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onBackground
        )
        Spacer(modifier = Modifier.weight(1.0f))
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                tint = BlueDark,
                contentDescription = null,
            )
        }
    }
}
