package com.algolia.instantsearch.guides.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.algolia.instantsearch.android.paging3.Paginator
import com.algolia.instantsearch.android.paging3.flow
import com.algolia.instantsearch.compose.filter.facet.FacetListState
import com.algolia.instantsearch.compose.highlighting.toAnnotatedString
import com.algolia.instantsearch.compose.item.StatsState
import com.algolia.instantsearch.compose.searchbox.SearchBox
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.core.selectable.list.SelectableItem
import com.algolia.instantsearch.guides.model.Product
import com.algolia.search.model.search.Facet
import kotlinx.coroutines.launch

@Composable
fun ProductsList(
    modifier: Modifier = Modifier,
    pagingHits: LazyPagingItems<Product>,
    listState: LazyListState
) {
    LazyColumn(modifier, listState) {
        items(pagingHits) { item ->
            if (item == null) return@items
            TextAnnotated(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                annotatedString = item.highlightedName?.toAnnotatedString(),
                default = item.name,
                style = MaterialTheme.typography.body1
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .width(1.dp)
            )
        }
    }
}

@Composable
fun TextAnnotated(
    modifier: Modifier,
    annotatedString: AnnotatedString?,
    default: String,
    style: TextStyle
) {
    if (annotatedString != null) {
        Text(modifier = modifier, text = annotatedString, style = style)
    } else {
        Text(modifier = modifier, text = default, style = style)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Search(
    modifier: Modifier = Modifier,
    searchBoxState: SearchBoxState,
    paginator: Paginator<Product>,
    statsText: StatsState<String>,
    facetList: FacetListState,
) {

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val listState = rememberLazyListState()
    val pagingHits = paginator.flow.collectAsLazyPagingItems()

    ModalBottomSheetLayout(
        modifier = modifier,
        sheetState = sheetState,
        sheetContent = { FacetList(facetList = facetList) },
        content = {
            Column(modifier) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    SearchBox(
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 12.dp, start = 12.dp),
                        searchBoxState = searchBoxState,
                        onValueChange = { _, _ -> scope.launch { listState.scrollToItem(0) } },
                    )

                    Card(Modifier.padding(top = 12.dp, end = 12.dp, start = 8.dp)) {
                        Icon(
                            modifier = Modifier
                                .clickable { scope.launch { sheetState.show() } }
                                .padding(horizontal = 12.dp)
                                .height(56.dp),
                            imageVector = Icons.Default.FilterList,
                            contentDescription = null,
                        )
                    }
                }
                Stats(modifier = Modifier.padding(start = 12.dp), stats = statsText.stats)
                ProductsList(
                    modifier = Modifier.fillMaxSize(),
                    pagingHits = pagingHits,
                    listState = listState
                )
            }
        }
    )
}

@Composable
fun Stats(modifier: Modifier = Modifier, stats: String) {
    Text(
        modifier = modifier,
        text = stats,
        style = MaterialTheme.typography.caption,
        maxLines = 1
    )
}

@Composable
fun FacetRow(
    modifier: Modifier = Modifier,
    selectableFacet: SelectableItem<Facet>
) {
    val (facet, isSelected) = selectableFacet
    Row(
        modifier = modifier.height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.weight(1f)) {
            Text(
                modifier = Modifier.alignByBaseline(),
                text = facet.value,
                style = MaterialTheme.typography.body1
            )
            Text(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .alignByBaseline(),
                text = facet.count.toString(),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.2f)
            )
        }
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
            )
        }
    }
}

@Composable
fun FacetList(
    modifier: Modifier = Modifier,
    facetList: FacetListState
) {
    Column(modifier) {
        Text(
            text = "Categories",
            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(14.dp)
        )
        LazyColumn(Modifier.background(MaterialTheme.colors.background)) {
            items(facetList.items) { item ->
                FacetRow(
                    modifier = Modifier
                        .clickable { facetList.onSelection?.invoke(item.first) }
                        .padding(horizontal = 14.dp),
                    selectableFacet = item,
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .width(1.dp)
                )
            }
        }
    }
}
