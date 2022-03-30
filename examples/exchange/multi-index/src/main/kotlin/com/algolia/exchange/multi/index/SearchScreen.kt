package com.algolia.exchange.multi.index

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Movie
import androidx.compose.runtime.Composable
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
    actorsState: HitsState<Actor>,
    moviesState: HitsState<Movie>,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {
        SearchBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            searchBoxState = searchBoxState,
        )

        Actors(actors = actorsState.hits)
        Movies(movies = moviesState.hits)
    }
}

@Composable
private fun Actors(
    modifier: Modifier = Modifier,
    actors: List<Actor>,
) {
    if (actors.isEmpty()) return

    Column(modifier) {
        SectionTitle(
            modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 4.dp),
            title = "Actors"
        )
        actors.forEach { actor ->
            Actor(actor = actor)
        }
    }
}

@Composable
private fun Actor(
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
private fun Movies(
    modifier: Modifier = Modifier,
    movies: List<Movie>
) {
    if (movies.isEmpty()) return

    Column(modifier) {
        SectionTitle(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            title = "Movies"
        )
        movies.forEach { movie ->
            Movie(movie = movie)
        }
    }
}

@Composable
private fun Movie(modifier: Modifier = Modifier, movie: Movie) {
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
        modifier = modifier,
        text = title, style = MaterialTheme.typography.subtitle2,
        color = MaterialTheme.colors.onBackground.copy(alpha = 0.4f),
    )
}
