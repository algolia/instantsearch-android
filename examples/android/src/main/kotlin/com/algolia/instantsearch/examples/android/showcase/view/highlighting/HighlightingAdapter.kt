package com.algolia.instantsearch.examples.android.showcase.view.highlighting

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.examples.android.databinding.ListItemHighlightingBinding
import com.algolia.instantsearch.examples.android.showcase.view.layoutInflater
import com.algolia.instantsearch.examples.android.showcase.view.list.movie.Movie
import com.algolia.instantsearch.examples.android.showcase.view.list.movie.MovieDiffUtil

class HighlightingAdapter : ListAdapter<Movie, HighlightingViewHolder>(MovieDiffUtil),
    HitsView<Movie> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HighlightingViewHolder {
        return HighlightingViewHolder(
            ListItemHighlightingBinding.inflate(parent.layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HighlightingViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun setHits(hits: List<Movie>) {
        submitList(hits)
    }
}
