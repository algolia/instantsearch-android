package com.algolia.exchange.query.suggestions

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
import androidx.compose.material.icons.filled.NorthWest
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
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
        if (showSuggestion) {
            LazyColumn {
                items(suggestionsState.hits) { suggestion ->
                    SuggestionRow(
                        modifier = Modifier.clickable {
                            searchBoxState.setText(suggestion.query, true)
                            showSuggestion = false
                        },
                        suggestion = suggestion,
                    )
                }
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
            .padding(12.dp)
    ) {
        Icon(
            modifier = Modifier.padding(start = 12.dp),
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
