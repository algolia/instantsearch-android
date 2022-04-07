package com.algolia.instantsearch.examples.showcase.view.list.movie

import androidx.recyclerview.widget.DiffUtil
import com.algolia.instantsearch.examples.showcase.shared.model.Movie

object MovieDiffUtil : DiffUtil.ItemCallback<Movie>() {

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
