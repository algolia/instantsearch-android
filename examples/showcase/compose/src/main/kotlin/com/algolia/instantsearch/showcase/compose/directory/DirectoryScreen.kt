package com.algolia.instantsearch.showcase.compose.directory

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.algolia.instantsearch.compose.hits.HitsState
import com.algolia.instantsearch.compose.searchbox.SearchBox
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.compose.searchbox.defaultSearchBoxColors
import com.algolia.instantsearch.showcase.compose.R
import com.algolia.instantsearch.showcase.compose.ui.GreyLight
import com.algolia.instantsearch.showcase.compose.ui.White

@Composable
fun Directory(
    searchBoxState: SearchBoxState,
    hitsState: HitsState<DirectoryItem>,
    onClick: (DirectoryItem.Item) -> Unit = {}
) {
    Column(Modifier.background(MaterialTheme.colors.background)) {
        Surface(elevation = 1.dp) {
            SearchBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                searchBoxState = searchBoxState,
                placeHolderText = stringResource(R.string.search_showcases),
                colors = defaultSearchBoxColors(
                    backgroundColor = MaterialTheme.colors.background,
                    onBackgroundColor = MaterialTheme.colors.onBackground,
                )
            )
        }
        LazyColumn {
            items(hitsState.hits) { item ->
                when (item) {
                    is DirectoryItem.Header -> Text(
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 6.dp
                        ),
                        text = item.name,
                        style = MaterialTheme.typography.subtitle2,
                        color = GreyLight,
                        maxLines = 1
                    )
                    is DirectoryItem.Item -> Surface(
                        elevation = 1.dp,
                        color = MaterialTheme.colors.surface,
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(White)
                                .clickable(onClick = { onClick(item) })
                                .padding(horizontal = 12.dp, vertical = 12.dp),
                            text = item.hit.name,
                            style = MaterialTheme.typography.body1,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }
}
