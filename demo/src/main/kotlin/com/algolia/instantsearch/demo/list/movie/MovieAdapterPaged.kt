package com.algolia.instantsearch.demo.list.movie

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.helper.android.inflate


class MovieAdapterPaged : PagedListAdapter<Movie, MovieViewHolder>(MovieDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(parent.inflate(R.layout.list_item_large))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = getItem(position)

        if (item != null) holder.bind(item)
    }
}