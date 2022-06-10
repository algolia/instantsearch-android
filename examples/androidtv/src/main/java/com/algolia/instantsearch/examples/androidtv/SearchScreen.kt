package com.algolia.instantsearch.examples.androidtv

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.algolia.instantsearch.compose.hits.HitsState
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.compose.searchbox.defaultSearchBoxColors
import com.algolia.instantsearch.examples.androidtv.ui.SearchBox
import com.algolia.instantsearch.examples.androidtv.ui.golden
import com.algolia.instantsearch.examples.androidtv.ui.grey
import com.algolia.instantsearch.examples.androidtv.ui.searchBackground
import com.algolia.instantsearch.examples.androidtv.ui.white

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchBoxState: SearchBoxState,
    hitsState: HitsState<Show>,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        SearchBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            searchBoxState = searchBoxState,
            colors = defaultSearchBoxColors(
                textColor = grey,
                backgroundColor = searchBackground,
                onBackgroundColor = white
            ),
            trailingIcon = { TrailingIcon(searchBoxState, white) }
        )
        Hits(modifier = Modifier.fillMaxSize(), hitsState = hitsState)
    }
}

@Composable
private fun Hits(modifier: Modifier = Modifier, hitsState: HitsState<Show>) {
    LazyColumn(modifier) {
        items(hitsState.hits) { hit ->
            HitRow(modifier = Modifier.fillMaxWidth(), hit = hit)
            Divider()
        }
    }
}

@Composable
private fun HitRow(modifier: Modifier = Modifier, hit: Show) {
    Row(modifier.padding(12.dp)) {
        Image(
            modifier = Modifier.size(width = 84.dp, height = 128.dp),
            contentScale = ContentScale.Crop,
            painter = rememberImagePainter(
                data = hit.posterUrl,
                builder = {
                    placeholder(android.R.drawable.ic_menu_report_image)
                    error(android.R.drawable.ic_menu_report_image)
                },
            ),
            contentDescription = hit.title,
        )

        Column(Modifier.padding(start = 8.dp)) {
            Text(
                text = hit.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.h6,
                color = Color.White,
            )
            Text(
                text = hit.genres.joinToString(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.caption,
                color = Color.Gray
            )
            Row(Modifier.padding(top = 12.dp)) {
                Icon(
                    modifier = Modifier.padding(end = 2.dp),
                    imageVector = Icons.Default.Star,
                    tint = golden,
                    contentDescription = null
                )
                Text(text = hit.voteAverage.toString(), color = golden)
            }

        }
    }
}

@Composable
internal fun TrailingIcon(searchBoxState: SearchBoxState, tint: Color) {
    if (searchBoxState.query.isNotEmpty()) {
        Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.clickable(
                onClick = { searchBoxState.setText(null) },
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            )
        )
    }
}
