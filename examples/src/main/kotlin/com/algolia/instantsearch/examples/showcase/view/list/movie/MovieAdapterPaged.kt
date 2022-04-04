package com.algolia.instantsearch.examples.showcase.view.list.movie

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.algolia.instantsearch.examples.databinding.ListItemLargeBinding
import com.algolia.instantsearch.examples.showcase.view.layoutInflater

class MovieAdapterPaged : PagingDataAdapter<Movie, MovieViewHolder>(MovieDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            ListItemLargeBinding.inflate(parent.layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = getItem(position)

        if (item != null) holder.bind(item)
    }
}
