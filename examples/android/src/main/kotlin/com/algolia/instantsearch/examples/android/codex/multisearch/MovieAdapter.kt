package com.algolia.instantsearch.examples.android.codex.multisearch

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.android.highlighting.toSpannedString
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.examples.android.databinding.ListItemSmallBinding
import com.algolia.instantsearch.examples.android.showcase.androidview.layoutInflater

class MovieAdapter : ListAdapter<Movie, MovieViewHolder>(MovieDiffUtil), HitsView<Movie> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            ListItemSmallBinding.inflate(parent.layoutInflater, parent, false)
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

class MovieViewHolder(private val binding: ListItemSmallBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: Movie) {
        binding.itemName.text = movie.highlightedTitle?.toSpannedString() ?: movie.title
    }
}

object MovieDiffUtil : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie) = oldItem.objectID == newItem.objectID
    override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem
}
