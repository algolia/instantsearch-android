package com.algolia.instantsearch.showcase.list.paging

import com.algolia.instantsearch.showcase.list.actor.ActorAdapterNested
import com.algolia.instantsearch.showcase.list.movie.MovieAdapterNested


sealed class PagingMultipleIndexItem {

    data class Header(val name: String): PagingMultipleIndexItem()

    data class Movies(val adapter: MovieAdapterNested) : PagingMultipleIndexItem()

    data class Actors(val adapter: ActorAdapterNested) : PagingMultipleIndexItem()
}