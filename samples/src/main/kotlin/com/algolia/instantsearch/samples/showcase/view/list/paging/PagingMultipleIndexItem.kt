package com.algolia.instantsearch.samples.showcase.view.list.paging

import com.algolia.instantsearch.samples.showcase.view.list.actor.ActorAdapterNested
import com.algolia.instantsearch.samples.showcase.view.list.movie.MovieAdapterNested


sealed class PagingMultipleIndexItem {

    data class Header(val name: String): PagingMultipleIndexItem()

    data class Movies(val adapter: MovieAdapterNested) : PagingMultipleIndexItem()

    data class Actors(val adapter: ActorAdapterNested) : PagingMultipleIndexItem()
}
