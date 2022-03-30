package com.algolia.instantsearch.showcase.list.movie

import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.showcase.databinding.ListItemMovieBinding
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
