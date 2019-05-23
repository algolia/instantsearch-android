package com.algolia.instantsearch.demo.list

import android.view.ViewGroup
import androidx.paging.PagedListAdapter


class MovieAdapterPaged : PagedListAdapter<Movie, MovieViewHolder>(DiffUtilMovie) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return createMovieViewHolder(parent)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        bind(holder, getItem(position))
    }
}