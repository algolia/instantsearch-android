package com.algolia.instantsearch.examples.showcase.androidview.list.paging

import com.algolia.instantsearch.examples.showcase.androidview.list.actor.ActorAdapterNested
import com.algolia.instantsearch.examples.showcase.androidview.list.movie.MovieAdapterNested


sealed class PagingMultipleIndexItem {

    data class Header(val name: String): PagingMultipleIndexItem()

    data class Movies(val adapter: MovieAdapterNested) : PagingMultipleIndexItem()

    data class Actors(val adapter: ActorAdapterNested) : PagingMultipleIndexItem()
}
