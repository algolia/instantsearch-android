package com.algolia.instantsearch.demo.list

import android.view.ViewGroup
import androidx.paging.PagedListAdapter


class PagedMovieAdapter : PagedListAdapter<Movie, MovieViewHolder>(MovieAdapterImpl), MovieAdapterImpl {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder = inflateViewHolder(parent)

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) = bindItem(getItem(position), holder)
}