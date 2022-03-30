package com.algolia.instantsearch.showcase.compose.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberImagePainter
import com.algolia.instantsearch.compose.highlighting.toAnnotatedString
import com.algolia.instantsearch.showcase.compose.model.Movie
import com.algolia.instantsearch.showcase.compose.ui.GreyDark
import java.util.*

@Composable
fun MoviesList(
    modifier: Modifier = Modifier,
    movies: List<Movie>,
    listState: LazyListState = rememberLazyListState()
) {
    LazyColumn(modifier, listState) {
        items(movies) { movie ->
            Surface(elevation = 1.dp) {
                MovieItem(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                        .padding(16.dp),
                    movie = movie
                )
            }
        }
    }
}

@Composable
fun MoviesList(
    modifier: Modifier = Modifier,
    movies: LazyPagingItems<Movie>,
    listState: LazyListState = rememberLazyListState()
) {
    LazyColumn(modifier, listState) {
        items(movies) { movie ->
            movie ?: return@items
            Surface(elevation = 1.dp) {
                MovieItem(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                        .padding(16.dp),
                    movie = movie
                )
            }
        }
    }
}

@Composable
fun MovieItem(modifier: Modifier = Modifier, movie: Movie) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Image(
            modifier = Modifier.size(68.dp),
            painter = rememberImagePainter(
                data = movie.image,
                builder = {
                    placeholder(android.R.drawable.ic_media_play)
                    error(android.R.drawable.ic_media_play)
                },
            ),
            contentDescription = "movie image",
        )

        Column(
            Modifier
                .padding(start = 16.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = titleOf(movie),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.body1
            )
            Text(
                text = genresOf(movie),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.body2,
                color = GreyDark
            )
            Text(
                text = actorsOf(movie),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.caption,
                color = GreyDark
            )
        }
    }
}


private fun titleOf(movie: Movie): AnnotatedString {
    return movie.highlightedTitle?.toAnnotatedString()
        ?: AnnotatedString(movie.title) + AnnotatedString(" ($movie.year)")
}

private fun genresOf(movie: Movie): AnnotatedString {
    return movie.highlightedGenres?.toAnnotatedString(SpanStyle(background = Color.Yellow))
        ?: AnnotatedString("unknown genre", SpanStyle(fontStyle = FontStyle.Italic))
}

private fun actorsOf(movie: Movie): String {
    return movie.highlightedActors?.let { list ->
        list.sortedByDescending { it.highlightedTokens.size }
            .joinToString { highlight ->
                highlight.tokens.joinToString("") {
                    if (it.highlighted) it.content.uppercase(Locale.getDefault()) else it.content
                }
            }
    } ?: ""
}

@Composable
fun MoviesHorizontalList(
    modifier: Modifier = Modifier,
    movies: LazyPagingItems<Movie>,
    listState: LazyListState = rememberLazyListState(),
) {
    LazyRow(modifier, listState) {
        items(movies) { movie ->
            movie ?: return@items
            MovieCardItem(
                modifier = Modifier
                    .width(IntrinsicSize.Min)
                    .padding(horizontal = 6.dp),
                movie = movie
            )
        }
    }
}

@Composable
fun MovieCardItem(modifier: Modifier = Modifier, movie: Movie) {
    Card(modifier) {
        Column {
            Image(
                modifier = Modifier.size(192.dp),
                contentScale = ContentScale.Crop,
                painter = rememberImagePainter(
                    data = movie.image,
                    builder = {
                        placeholder(android.R.drawable.ic_media_play)
                        error(android.R.drawable.ic_media_play)
                    },
                ),
                contentDescription = "movie image",
            )
            Column(Modifier.padding(8.dp)) {
                Text(
                    text = titleOf(movie),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.subtitle2
                )
                Text(
                    text = genresOf(movie),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body2,
                    color = GreyDark
                )
                Text(
                    text = movie.year,
                    maxLines = 1,
                    style = MaterialTheme.typography.caption,
                    color = GreyDark
                )
            }
        }
    }
}


