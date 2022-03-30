package com.algolia.exchange.query.suggestions.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.algolia.instantsearch.compose.highlighting.toAnnotatedString
import com.algolia.instantsearch.compose.hits.HitsState
import com.algolia.instantsearch.compose.searchbox.SearchBox
import com.algolia.instantsearch.compose.searchbox.SearchBoxState

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchBoxState: SearchBoxState,
    suggestionsState: HitsState<Suggestion>,
    recentSearches: MutableList<String>,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        var showSuggestion by remember { mutableStateOf(false) }
        SearchBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            searchBoxState = searchBoxState,
            onValueChange = { query, isSubmit ->
                showSuggestion = query.isNotEmpty() && suggestionsState.hits.isNotEmpty()
                if (isSubmit) recentSearches.addIfNotExist(query)
            }
        )

        if (showSuggestion) {
            RecentSearches(
                recent = recentSearches,
                onClearAll = recentSearches::clear,
                onClear = { recentSearches.remove(it) }
            ) { recentSearch ->
                searchBoxState.setText(recentSearch, true)
                showSuggestion = false
            }

            Suggestions(suggestionsState = suggestionsState) { suggestion ->
                searchBoxState.setText(suggestion, true)
                recentSearches.addIfNotExist(suggestion)
                showSuggestion = false
            }
        }
    }
}

@Composable
private fun RecentSearches(
    modifier: Modifier = Modifier,
    recent: List<String>,
    onClear: (String) -> Unit,
    onClearAll: () -> Unit,
    onClick: (String) -> Unit,
) {
    if (recent.isEmpty()) return

    Column(modifier) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionTitle(Modifier.weight(1.0f), title = "Recent searches")
            Icon(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .clickable(onClick = onClearAll),
                imageVector = Icons.Default.ClearAll,
                tint = MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
                contentDescription = null
            )
        }

        LazyColumn {
            items(recent) { recentSearch ->
                RecentSearchRow(recentSearch = recentSearch, onClick = onClick, onClear = onClear)
            }
        }
    }
}

@Composable
private fun RecentSearchRow(
    modifier: Modifier = Modifier,
    recentSearch: String,
    onClear: (String) -> Unit = {},
    onClick: (String) -> Unit = {},
) {
    Row(
        modifier
            .background(MaterialTheme.colors.surface)
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Row(
            Modifier
                .weight(1.0f)
                .clickable { onClick(recentSearch) }
        ) {
            Icon(
                imageVector = Icons.Default.Restore,
                tint = MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
                contentDescription = null
            )
            Text(
                text = recentSearch,
                modifier = Modifier
                    .padding(start = 12.dp),
            )
        }

        Icon(
            imageVector = Icons.Default.Clear,
            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
            contentDescription = null,
            modifier = Modifier.clickable { onClear(recentSearch) }
        )
    }
}

@Composable
private fun Suggestions(
    modifier: Modifier = Modifier,
    suggestionsState: HitsState<Suggestion>,
    onClick: (String) -> Unit = {}
) {
    Column(modifier) {
        SectionTitle(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            title = "Suggestions"
        )
        LazyColumn {
            items(suggestionsState.hits) { suggestion ->
                SuggestionRow(suggestion = suggestion, onClick = onClick)
            }
        }
    }
}

@Composable
private fun SuggestionRow(
    modifier: Modifier = Modifier,
    suggestion: Suggestion,
    onClick: (String) -> Unit
) {
    Row(
        modifier
            .background(MaterialTheme.colors.surface)
            .clickable { onClick(suggestion.query) }
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
private fun SectionTitle(modifier: Modifier = Modifier, title: String) {
    Text(
        modifier = modifier,
        text = title, style = MaterialTheme.typography.subtitle2,
        color = MaterialTheme.colors.onBackground.copy(alpha = 0.4f),
    )
}

fun MutableList<String>.addIfNotExist(query: String) {
    val element = query.trim()
    if (!contains(element)) this.add(element)
}
