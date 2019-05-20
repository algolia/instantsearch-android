package com.algolia.instantsearch.demo.list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.inflate

interface MovieAdapterImpl {
    fun inflateViewHolder(parent: ViewGroup) =
        MovieViewHolder(parent.inflate(R.layout.list_item_large))

    fun bindItem(item: Movie?, holder: MovieViewHolder) {
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