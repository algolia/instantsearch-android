package com.algolia.instantsearch.showcase.compose.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.algolia.instantsearch.compose.searchbox.SearchBox
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.showcase.compose.ui.ShowcaseTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Preview
@Composable
fun TitleTopBarPreview() {
    ShowcaseTheme {
        TitleTopBar(title = "Filters")
    }
}

@Composable
fun TitleTopBar(modifier: Modifier = Modifier, title: String = "", onBackPressed: () -> Unit = {}) {
    TopAppBar(
        modifier = modifier,
        title = { Text(title) },
        backgroundColor = MaterialTheme.colors.background,
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .clickable(
                        onClick = onBackPressed,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    )
                    .padding(start = 16.dp)
            )
        },
    )
}

@Preview
@Composable
fun SearchTopBarPreview() {
    val searchBoxState = SearchBoxState()
    ShowcaseTheme {
        SearchTopBar(searchBoxState = searchBoxState, icon = Icons.Default.Info)
    }
}

@Composable
fun SearchTopBar(
    modifier: Modifier = Modifier,
    placeHolderText: String = "Search...",
    searchBoxState: SearchBoxState,
    onBackPressed: () -> Unit = {},
    lazyListState: LazyListState,
    scope: CoroutineScope = rememberCoroutineScope()
) {
    SearchTopBar(
        modifier,
        placeHolderText,
        searchBoxState,
        onBackPressed,
        listOf(lazyListState),
        scope,
    )
}

@Composable
fun SearchTopBar(
    modifier: Modifier = Modifier,
    placeHolderText: String = "Search...",
    searchBoxState: SearchBoxState,
    onBackPressed: () -> Unit = {},
    lazyListStates: List<LazyListState>? = null,
    scope: CoroutineScope = rememberCoroutineScope(),
    icon: ImageVector? = null,
    onIconClick: () -> Unit = {}
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Row {
                SearchBox(
                    modifier = Modifier.fillMaxWidth(),
                    searchBoxState = searchBoxState,
                    placeHolderText = placeHolderText,
                    textStyle = MaterialTheme.typography.body1,
                    onValueChange = { _, _ ->
                        lazyListStates?.let { listStates ->
                            scope.launch { listStates.forEach { it.scrollToItem(0) } }
                        }
                    },
                    elevation = 0.dp
                )
            }
        },
        backgroundColor = MaterialTheme.colors.surface,
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .clickable(
                        onClick = onBackPressed,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    )
                    .padding(start = 16.dp)
            )
        },
        actions = {
            icon?.let {
                Icon(it, null,
                    Modifier
                        .padding(4.dp)
                        .clickable(onClick = onIconClick))
            }
        }
    )
}
