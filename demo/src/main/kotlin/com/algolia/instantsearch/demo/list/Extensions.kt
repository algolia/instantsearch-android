package com.algolia.instantsearch.demo.list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.inflate

fun RecyclerView.Adapter<MovieViewHolder>.createMovieViewHolder(parent: ViewGroup): MovieViewHolder {
    return MovieViewHolder(parent.inflate(R.layout.list_item_large))
}

fun RecyclerView.Adapter<MovieViewHolder>.bind(holder: MovieViewHolder, item: Movie?) {
    if (item != null) holder.bind(item)
}

object DiffUtilMovie : DiffUtil.ItemCallback<Movie>() {

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