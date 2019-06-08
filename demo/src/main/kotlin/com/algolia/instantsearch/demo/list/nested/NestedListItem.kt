package com.algolia.instantsearch.demo.list.nested

import com.algolia.instantsearch.demo.list.actor.ActorAdapterNested
import com.algolia.instantsearch.demo.list.movie.MovieAdapterNested


sealed class NestedListItem {

    data class Header(val name: String): NestedListItem()

    data class Movies(val adapter: MovieAdapterNested) : NestedListItem()

    data class Actors(val adapter: ActorAdapterNested) : NestedListItem()
}