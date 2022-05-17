package com.algolia.instantsearch.examples.android.showcase.androidview.list.movie

import androidx.recyclerview.widget.DiffUtil

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
