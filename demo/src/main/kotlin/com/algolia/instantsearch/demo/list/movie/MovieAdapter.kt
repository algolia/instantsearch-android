package com.algolia.instantsearch.demo.list.movie

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.helper.android.inflate


class MovieAdapter : ListAdapter<Movie, MovieViewHolder>(MovieDiffUtil), HitsView<Movie> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(parent.inflate(R.layout.list_item_large))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = getItem(position)

        if (item != null) holder.bind(item)
    }

    override fun setHits(hits: List<Movie>) {
        submitList(hits)
    }
}