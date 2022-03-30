package com.algolia.instantsearch.showcase.compose.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.algolia.instantsearch.showcase.compose.R
import com.algolia.instantsearch.showcase.compose.highlight
import com.algolia.instantsearch.showcase.compose.ui.ShowcaseTheme
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.FilterGroup
import com.algolia.search.model.filter.NumericOperator

@Preview
@Composable
fun HeaderFilterPreview() {
    val color = Attribute("color")
    val price = Attribute("price")
    ShowcaseTheme {
        HeaderFilter(
            filterGroups = setOf(
                FilterGroup.And.Facet(Filter.Facet(color, "red"), Filter.Facet(color, "green")),
                FilterGroup.And.Tag(Filter.Tag("mobile")),
                FilterGroup.And.Numeric(
                    Filter.Numeric(price, NumericOperator.NotEquals, 42),
                    Filter.Numeric(price, 0..100)
                ),
            ),
            stats = "128 hits"
        )
    }
}

@Composable
fun HeaderFilter(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.filters),
    filterHeader: HeaderFilterConnector,
) {
    HeaderFilter(
        modifier = modifier,
        title = title,
        filterGroups = filterHeader.filterGroups,
        onClear = filterHeader.clearAll::clear,
        stats = filterHeader.hitsStats.stats,
        colors = filterHeader.filterColors
    )
}

@Composable
fun HeaderFilter(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.filters),
    filterGroups: Set<FilterGroup<*>>,
    onClear: () -> Unit = {},
    stats: String = "",
    colors: Map<String, Color> = emptyMap()
) {
    Card(modifier) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = title,
                    style = MaterialTheme.typography.h6
                )
                Icon(
                    modifier = Modifier.clickable { onClear() },
                    imageVector = Icons.Default.Delete,
                    contentDescription = "clear",
                )
            }
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = filterGroups.highlight(colors = colors),
                style = MaterialTheme.typography.body2
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stats,
                style = MaterialTheme.typography.caption
            )
        }
    }
}
