package com.algolia.instantsearch.showcase.list.movie

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.showcase.databinding.ListItemLargeBinding
import com.algolia.instantsearch.showcase.layoutInflater

class MovieAdapter : ListAdapter<Movie, MovieViewHolder>(MovieDiffUtil), HitsView<Movie> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            ListItemLargeBinding.inflate(parent.layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = getItem(position)

        if (item != null) holder.bind(item)
    }

    override fun setHits(hits: List<Movie>) {
        submitList(hits)
    }
}
