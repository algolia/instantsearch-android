@file:OptIn(ExperimentalFoundationApi::class)

package com.algolia.instantsearch.examples.android.codex.multipleindex

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Movie
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.algolia.instantsearch.compose.highlighting.toAnnotatedString
import com.algolia.instantsearch.compose.hits.HitsState
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.examples.android.showcase.compose.ui.component.SearchBox

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchBoxState: SearchBoxState,
    actorsState: HitsState<Actor>,
    moviesState: HitsState<Movie>,
) {
    Scaffold(modifier = modifier, topBar = {
        SearchBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            searchBoxState = searchBoxState,
        )
    }) { paddings ->
        LazyColumn(
            Modifier
                .padding(paddings)
                .fillMaxSize()
        ) {
            stickyHeader { SectionTitle(title = "Actors") }
            items(actorsState.hits) { actor -> ActorItem(actor = actor) }

            stickyHeader { SectionTitle(title = "Movies") }
            items(moviesState.hits) { movie -> MovieItem(movie = movie) }
        }
    }
}

@Composable
private fun ActorItem(
    modifier: Modifier = Modifier,
    actor: Actor
) {
    Row(
        modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
            contentDescription = null
        )
        Text(
            text = actor.highlightedName?.toAnnotatedString() ?: AnnotatedString(actor.name),
            modifier = Modifier.padding(start = 12.dp),
        )
    }
}

@Composable
private fun MovieItem(modifier: Modifier = Modifier, movie: Movie) {
    Row(
        modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Movie,
            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
            contentDescription = null
        )
        Text(
            text = movie.highlightedTitle?.toAnnotatedString() ?: AnnotatedString(movie.title),
            modifier = Modifier.padding(start = 12.dp),
        )
    }
}

@Composable
private fun SectionTitle(modifier: Modifier = Modifier, title: String) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        text = title, style = MaterialTheme.typography.subtitle2,
        color = MaterialTheme.colors.onBackground.copy(alpha = 0.4f),
    )
}
