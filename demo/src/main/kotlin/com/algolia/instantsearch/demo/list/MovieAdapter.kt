package com.algolia.instantsearch.demo.list

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.inflate


class MovieAdapter : PagedListAdapter<Movie, MovieViewHolder>(MovieAdapter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
        MovieViewHolder(parent.inflate(R.layout.list_item_large))


    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = getItem(position)

        if (item != null) holder.bind(item)
    }

    companion object : DiffUtil.ItemCallback<Movie>() {

        override fun areItemsTheSame(
            oldItem: Movie,
            newItem: Movie
        ): Boolean {
            return oldItem.objectID == newItem.objectID
        }

        override fun areContentsTheSame(
            oldItem: Movie,
            newItem: Movie
        ): Boolean {
            return oldItem == newItem
        }
    }
}