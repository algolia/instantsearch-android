package com.algolia.instantsearch.showcase.compose.filter.current.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.algolia.instantsearch.compose.filter.current.FilterCurrentState
import com.algolia.instantsearch.showcase.compose.ui.ShowcaseTheme
import kotlin.math.max

@Composable
fun FilterChips(
    modifier: Modifier = Modifier,
    filterCurrentState: FilterCurrentState,
    rows: Int
) {
    Row(modifier = modifier.horizontalScroll(rememberScrollState()),
        content = {
            StaggeredGrid(rows = rows) {
                filterCurrentState.filters.forEach { (filterGroupAndID, filter) ->
                    Chip(modifier = Modifier.padding(8.dp), text = filter) {
                        filterCurrentState.selectFilter(filterGroupAndID)
                    }
                }
            }
        }
    )
}

@Preview
@Composable
fun ChipPreview() {
    ShowcaseTheme {
        Chip(text = "price: 0 to 100")
    }
}

@Composable
fun Chip(
    modifier: Modifier = Modifier,
    text: String,
    isClosable: Boolean = true,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier,
        elevation = 1.dp,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colors.primary
    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onPrimary,
                modifier = Modifier.padding(4.dp)
            )

            if (isClosable) IconClose(
                modifier = Modifier.height(14.dp),
                background = MaterialTheme.colors.background,
                iconColor = MaterialTheme.colors.primary
            )
        }
    }
}

@Composable
fun IconClose(modifier: Modifier = Modifier, background: Color, iconColor: Color) {
    Box(
        modifier
            .wrapContentSize(Alignment.Center)
            .clip(CircleShape)
            .background(background)
    ) {
        Icon(imageVector = Icons.Default.Close, contentDescription = "close", tint = iconColor)
    }
}

@Preview
@Composable
fun StaggeredGridPreview() {
    ShowcaseTheme {
        val filters = listOf("green", "mobile", "price != 42", "price: 0 to 100", "red")
        StaggeredGrid {
            filters.forEach { filter ->
                Chip(modifier = Modifier.padding(8.dp), text = filter)
            }
        }
    }
}

@Composable
fun StaggeredGrid(
    modifier: Modifier = Modifier,
    rows: Int = 2,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->

        // Keep track of the width of each row
        val rowWidths = IntArray(rows) { 0 }

        // Keep track of the max height of each row
        val rowHeights = IntArray(rows) { 0 }

        // Don't constrain child views further, measure them with given constraints
        // List of measured children
        val placeables = measurables.mapIndexed { index, measurable ->
            // Measure each child
            val placeable = measurable.measure(constraints)

            // Track the width and max height of each row
            val row = index % rows
            rowWidths[row] += placeable.width
            rowHeights[row] = max(rowHeights[row], placeable.height)

            placeable
        }

        // Grid's width is the widest row
        val width = rowWidths.maxOrNull()
            ?.coerceIn(constraints.minWidth.rangeTo(constraints.maxWidth))
            ?: constraints.minWidth

        // Grid's height is the sum of the tallest element of each row
        // coerced to the height constraints
        val height = rowHeights.sumOf { it }
            .coerceIn(constraints.minHeight.rangeTo(constraints.maxHeight))

        // Y of each row, based on the height accumulation of previous rows
        val rowY = IntArray(rows) { 0 }
        for (i in 1 until rows) {
            rowY[i] = rowY[i - 1] + rowHeights[i - 1]
        }

        // Set the size of the parent layout
        layout(width, height) {
            // x co-ord we have placed up to, per row
            val rowX = IntArray(rows) { 0 }

            placeables.forEachIndexed { index, placeable ->
                val row = index % rows
                placeable.placeRelative(
                    x = rowX[row],
                    y = rowY[row]
                )
                rowX[row] += placeable.width
            }
        }
    }
}
