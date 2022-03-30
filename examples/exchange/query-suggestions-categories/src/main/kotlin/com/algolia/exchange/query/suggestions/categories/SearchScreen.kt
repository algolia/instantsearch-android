package com.algolia.exchange.query.suggestions.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
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
import com.algolia.instantsearch.core.highlighting.DefaultPostTag
import com.algolia.instantsearch.core.highlighting.DefaultPreTag
import com.algolia.instantsearch.core.highlighting.HighlightTokenizer
import com.algolia.search.model.search.Facet

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchBoxState: SearchBoxState,
    suggestionsState: HitsState<Suggestion>,
    categoriesState: HitsState<Facet>,
) {
    val scrollState = rememberScrollState()
    Column(modifier = modifier
        .fillMaxWidth()
        .verticalScroll(scrollState)) {
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
            Categories(
                categories = categoriesState.hits
            )

            Suggestions(suggestionsState = suggestionsState) { suggestion ->
                searchBoxState.setText(suggestion, true)
                showSuggestion = false
            }
        }
    }
}

@Composable
private fun Categories(
    modifier: Modifier = Modifier,
    categories: List<Facet>,
) {
    Column(modifier) {
        SectionTitle(
            modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 4.dp),
            title = "Categories"
        )

        Column {
            categories.forEach { category ->
                CategoryRow(category = category)

            }
        }
    }
}

@Composable
private fun CategoryRow(
    modifier: Modifier = Modifier,
    category: Facet
) {
    Row(
        modifier
            .background(MaterialTheme.colors.surface)
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Category,
            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
            contentDescription = null
        )
        Text(
            text = category.highlighted.toAnnotatedString(),
            modifier = Modifier.padding(start = 12.dp),
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
        Column {
            suggestionsState.hits.forEach { suggestion ->
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

private fun String.toAnnotatedString(): AnnotatedString {
    return HighlightTokenizer(DefaultPreTag, DefaultPostTag)(this).toAnnotatedString()
}
