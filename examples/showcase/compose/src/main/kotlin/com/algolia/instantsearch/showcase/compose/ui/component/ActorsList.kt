package com.algolia.instantsearch.showcase.compose.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.algolia.instantsearch.showcase.compose.model.Actor


@Composable
fun ActorsList(modifier: Modifier = Modifier, actors: List<Actor>) {
    LazyColumn(modifier) {
        items(actors) { actor ->
            Surface(elevation = 1.dp) {
                ActorItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    actor = actor
                )
            }
        }
    }
}

@Composable
fun ActorItem(modifier: Modifier = Modifier, actor: Actor) {
    Text(modifier = modifier, text = actor.name)
}

@Composable
fun ActorsHorizontalList(
    modifier: Modifier = Modifier,
    actors: LazyPagingItems<Actor>,
    listState: LazyListState = rememberLazyListState()
) {
    LazyRow(modifier, listState) {
        items(actors) { actor ->
            actor ?: return@items
            ActorCardItem(
                modifier = Modifier.padding(horizontal = 6.dp),
                actor = actor
            )
        }
    }
}

@Composable
fun ActorCardItem(modifier: Modifier = Modifier, actor: Actor) {
    Card(modifier) {
        Text(
            modifier = Modifier.padding(12.dp),
            text = actor.name,
            style = MaterialTheme.typography.body1
        )
    }
}
