package com.algolia.instantsearch.examples.showcase.view.list.movie

import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.examples.databinding.ListItemMovieBinding
import com.algolia.instantsearch.examples.showcase.shared.model.Movie
import com.bumptech.glide.Glide

class MovieViewHolderNested(private val binding: ListItemMovieBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: Movie) {
        binding.itemTitle.text = movie.title
        binding.itemSubtitle.text = movie.genre.sorted().joinToString { it }
        binding.itemCaption.text = movie.year
        Glide.with(binding.root)
            .load(movie.image).placeholder(android.R.drawable.ic_media_play)
            .centerCrop()
            .into(binding.itemImage)
    }
}
