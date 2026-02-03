package com.algolia.instantsearch.examples.android.showcase.compose.ui.component

import android.app.Activity
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.LocalIndication
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.examples.android.showcase.compose.ui.ShowcaseTheme
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
fun TitleTopBar(
    modifier: Modifier = Modifier,
    title: String = "",
    onBackClick: (() -> Unit)? = null,
) {
    val activity = (LocalContext.current as? Activity)
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val handleBack: () -> Unit = onBackClick ?: {
        backDispatcher?.onBackPressed()
        activity?.finish()
        Unit
    }
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
                        onClick = handleBack,
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
    lazyListState: LazyListState,
    scope: CoroutineScope = rememberCoroutineScope(),
    onBackClick: (() -> Unit)? = null,
) {
    SearchTopBar(
        modifier,
        placeHolderText,
        searchBoxState,
        listOf(lazyListState),
        scope,
        onBackClick = onBackClick,
    )
}

@Composable
fun SearchTopBar(
    modifier: Modifier = Modifier,
    placeHolderText: String = "Search...",
    searchBoxState: SearchBoxState,
    lazyListStates: List<LazyListState>? = null,
    scope: CoroutineScope = rememberCoroutineScope(),
    icon: ImageVector? = null,
    onIconClick: () -> Unit = {},
    onBackClick: (() -> Unit)? = null,
) {
    val activity = (LocalContext.current as? Activity)
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val handleBack: () -> Unit = onBackClick ?: {
        backDispatcher?.onBackPressed()
        activity?.finish()
        Unit
    }
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
                        onClick = handleBack,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    )
                    .padding(start = 16.dp)
            )
        },
        actions = {
            icon?.let {
                Icon(
                    it, null,
                    Modifier
                        .padding(4.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = LocalIndication.current,
                            onClick = onIconClick
                        )
                )
            }
        }
    )
}
