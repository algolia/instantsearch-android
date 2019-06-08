package com.algolia.instantsearch.demo.list.nested

import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.dip
import com.algolia.instantsearch.demo.inflate
import com.algolia.instantsearch.demo.list.actor.Actor
import com.algolia.instantsearch.demo.list.actor.ActorAdapterNested
import com.algolia.instantsearch.demo.list.movie.Movie
import com.algolia.instantsearch.demo.list.movie.MovieAdapterNested
import com.algolia.instantsearch.helper.android.list.SearcherMultipleIndexDataSource
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex


class NestedListAdapter(
    val searcher: SearcherMultipleIndex,
    val lifecycleOwner: LifecycleOwner
) : ListAdapter<NestedListItem, NestedListViewHolder>(this) {

    private enum class ViewType {
        Header,
        Movies,
        Actors
    }

    private val pagedListConfig = PagedList.Config.Builder().setPageSize(10).build()
    private val moviesFactory = SearcherMultipleIndexDataSource.Factory(searcher, 0, Movie.serializer())
    private val actorsFactory = SearcherMultipleIndexDataSource.Factory(searcher, 1, Actor.serializer())
    private val movies = LivePagedListBuilder<Int, Movie>(moviesFactory, pagedListConfig).build()
    private val actors = LivePagedListBuilder<Int, Actor>(actorsFactory, pagedListConfig).build()
    private val actorsAdapter = ActorAdapterNested().also {
        actors.observe(lifecycleOwner, Observer { hits -> it.submitList(hits) })
    }
    private val moviesAdapter = MovieAdapterNested().also {
        movies.observe(lifecycleOwner, Observer { hits -> it.submitList(hits) })
    }

    init {
        submitList(
            listOf(
                NestedListItem.Header("Movies"),
                NestedListItem.Movies(moviesAdapter),
                NestedListItem.Header("Actors"),
                NestedListItem.Actors(actorsAdapter)
            )
        )
    }

    private fun recyclerView(parent: ViewGroup): RecyclerView {
        return RecyclerView(parent.context).also {
            it.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            it.setPadding(parent.context.dip(8), 0, parent.context.dip(8), 0)
            it.clipToPadding = false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NestedListViewHolder {
        return when (ViewType.values()[viewType]) {
            ViewType.Header -> NestedListViewHolder.Header(parent.inflate<TextView>(R.layout.header_item))
            ViewType.Movies -> NestedListViewHolder.Movies(recyclerView(parent))
            ViewType.Actors -> NestedListViewHolder.Actors(recyclerView(parent))
        }
    }

    override fun onBindViewHolder(holder: NestedListViewHolder, position: Int) {
        val item = getItem(position)

        when (holder) {
            is NestedListViewHolder.Actors -> holder.bind(item as NestedListItem.Actors)
            is NestedListViewHolder.Movies -> holder.bind(item as NestedListItem.Movies)
            is NestedListViewHolder.Header -> holder.bind(item as NestedListItem.Header)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is NestedListItem.Header -> ViewType.Header
            is NestedListItem.Movies -> ViewType.Movies
            is NestedListItem.Actors -> ViewType.Actors
        }.ordinal
    }

    private companion object : DiffUtil.ItemCallback<NestedListItem>() {

        override fun areItemsTheSame(oldItem: NestedListItem, newItem: NestedListItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: NestedListItem, newItem: NestedListItem): Boolean {
            return oldItem == newItem
        }
    }
}