package com.algolia.exchange.query.suggestions.hits

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NorthWest
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.algolia.instantsearch.compose.highlighting.toAnnotatedString
import com.algolia.instantsearch.compose.hits.HitsState
import com.algolia.instantsearch.compose.searchbox.SearchBox
import com.algolia.instantsearch.compose.searchbox.SearchBoxState

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchBoxState: SearchBoxState,
    suggestionsState: HitsState<Suggestion>,
    hitsState: HitsState<Product>,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        var showSuggestion by remember { mutableStateOf(false) }
        SearchBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            searchBoxState = searchBoxState,
            onValueChange = { query, _ ->
                showSuggestion = query.isNotEmpty() && suggestionsState.hits.isNotEmpty()
            }
        )
        Suggestions(suggestionsState = suggestionsState, showSuggestion = showSuggestion) { query ->
            searchBoxState.setText(query, true)
            showSuggestion = false
        }
        Results(hitsState)
    }
}

@Composable
private fun Suggestions(
    modifier: Modifier = Modifier,
    suggestionsState: HitsState<Suggestion>,
    showSuggestion: Boolean,
    onClick: (String) -> Unit = {}
) {
    if (!showSuggestion) return
    Column(modifier) {
        SectionTitle("Suggestions")
        LazyColumn {
            items(suggestionsState.hits) { suggestion ->
                SuggestionRow(
                    modifier = Modifier.clickable { onClick(suggestion.query) },
                    suggestion = suggestion,
                )
            }
        }
    }
}

@Composable
private fun SuggestionRow(
    modifier: Modifier = Modifier,
    suggestion: Suggestion
) {
    Row(
        modifier
            .background(MaterialTheme.colors.surface)
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
            contentDescription = null
        )
        val text = suggestion.highlightedQuery?.toAnnotatedString()
            ?: AnnotatedString(suggestion.query)
        Text(
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f), text = text
        )
        Icon(
            imageVector = Icons.Default.NorthWest,
            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
            contentDescription = null
        )
    }
}

@Composable
private fun Results(hitsState: HitsState<Product>) {
    SectionTitle("Results")
    LazyColumn {
        items(hitsState.hits) { product ->
            ProductRow(product = product)
        }
    }
}

@Composable
private fun ProductRow(modifier: Modifier = Modifier, product: Product) {
    Row(
        modifier
            .background(MaterialTheme.colors.surface)
            .padding(12.dp)
    ) {
        Image(
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Crop,
            painter = rememberImagePainter(
                data = product.image,
                builder = {
                    placeholder(android.R.drawable.ic_menu_report_image)
                    error(android.R.drawable.ic_menu_report_image)
                },
            ),
            contentDescription = product.name,
        )

        Column(Modifier.padding(start = 8.dp)) {
            Text(
                text = product.name,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.subtitle2
            )
            Text(
                text = product.description,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.caption,
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title, style = MaterialTheme.typography.subtitle2,
        color = MaterialTheme.colors.onBackground.copy(alpha = 0.8f),
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
    )
}
