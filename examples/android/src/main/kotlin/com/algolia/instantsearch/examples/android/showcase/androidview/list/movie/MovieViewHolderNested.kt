package com.algolia.instantsearch.examples.android.showcase.androidview.list.movie

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.algolia.instantsearch.examples.android.databinding.ListItemMovieBinding

class MovieViewHolderNested(private val binding: ListItemMovieBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: Movie) {
        binding.itemTitle.text = movie.title
        binding.itemSubtitle.text = movie.genre.sorted().joinToString { it }
        binding.itemCaption.text = movie.year
        binding.itemImage.load(movie.image) {
            placeholder(android.R.drawable.ic_media_play)
            error(android.R.drawable.ic_media_play)
        }
    }
}
