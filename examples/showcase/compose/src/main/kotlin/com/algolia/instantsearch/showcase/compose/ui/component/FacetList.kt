package com.algolia.instantsearch.showcase.compose.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.algolia.instantsearch.compose.filter.facet.FacetListState
import com.algolia.instantsearch.core.selectable.list.SelectableItem
import com.algolia.instantsearch.filter.facet.FacetListPresenterImpl
import com.algolia.instantsearch.filter.facet.FacetSortCriterion
import com.algolia.instantsearch.filter.state.FilterGroupID
import com.algolia.instantsearch.filter.state.FilterOperator
import com.algolia.instantsearch.filter.state.groupAnd
import com.algolia.instantsearch.showcase.compose.ui.BlueDark
import com.algolia.instantsearch.showcase.compose.ui.HoloRedDark
import com.algolia.instantsearch.showcase.compose.ui.ShowcaseTheme
import com.algolia.instantsearch.showcase.compose.ui.White
import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Facet

@Preview
@Composable
fun FacetListPreview() {
    val color = Attribute("color")
    val groupColor = groupAnd(color)
    val presenter = FacetListPresenterImpl(
        listOf(
            FacetSortCriterion.IsRefined,
            FacetSortCriterion.AlphabeticalAscending
        ), limit = 3
    )
    ShowcaseTheme {
        FacetList(
            presenter = presenter,
            filterGroupID = groupColor,
            titleColor = HoloRedDark,
            facetListState = FacetListState(
                listOf(
                    Facet("green", 2) to true,
                    Facet("red", 5) to false,
                )
            )
        )
    }
}

@Composable
fun FacetList(
    modifier: Modifier = Modifier,
    presenter: FacetListPresenterImpl,
    filterGroupID: FilterGroupID,
    titleColor: Color,
    facetListState: FacetListState,

    ) {
    FacetList(
        modifier = modifier,
        title = formatTitle(presenter, filterGroupID),
        titleColor = titleColor,
        facetListState = facetListState
    )
}

@Composable
fun FacetList(
    modifier: Modifier = Modifier,
    title: String? = null,
    titleColor: Color? = null,
    facetListState: FacetListState
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        if (title != null) {
            Text(
                modifier = Modifier.padding(4.dp),
                text = title,
                color = titleColor ?: Color.Unspecified,
                style = MaterialTheme.typography.caption
            )
        }

        Surface(
            contentColor = White,
            modifier = Modifier.padding(horizontal = 4.dp),
            elevation = 1.dp,
        ) {
            Column {
                facetListState.items.forEachIndexed { index, selectableFacet ->
                    FacetRow(
                        modifier = Modifier.fillMaxWidth(),
                        selectableFacet = selectableFacet,
                        onClick = { facetListState.onSelection?.invoke(it) }
                    )
                    if (index != facetListState.items.lastIndex) Divider()
                }
            }
        }
    }
}

@Composable
fun FacetRow(
    modifier: Modifier = Modifier,
    selectableFacet: SelectableItem<Facet>,
    onClick: (Facet) -> Unit = {},
) {
    val (facet, isSelected) = selectableFacet
    Row(
        modifier = modifier
            .height(50.dp)
            .clickable(onClick = { onClick(facet) })
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = facet.value,
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
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = facet.count.toString(),
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onBackground.copy(alpha = 0.2f)
        )
    }
}

private fun formatTitle(presenter: FacetListPresenterImpl, filterGroupID: FilterGroupID): String {
    val criteria = presenter.sortBy.joinToString("-") { it.format() }
    val operator = when (filterGroupID.operator) {
        FilterOperator.And -> "And"
        FilterOperator.Or -> "Or"
    }

    return "$operator, $criteria, l=${presenter.limit}"
}

private fun FacetSortCriterion.format(): String {
    return when (this) {
        FacetSortCriterion.IsRefined -> name
        FacetSortCriterion.CountAscending -> "CountAsc"
        FacetSortCriterion.CountDescending -> "CountDesc"
        FacetSortCriterion.AlphabeticalAscending -> "AlphaAsc"
        FacetSortCriterion.AlphabeticalDescending -> "AlphaDesc"
    }
}
