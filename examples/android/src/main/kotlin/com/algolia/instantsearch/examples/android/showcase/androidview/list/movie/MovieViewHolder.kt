package com.algolia.instantsearch.examples.android.showcase.androidview.list.movie

import android.text.TextUtils
import androidx.core.text.buildSpannedString
import androidx.core.text.italic
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.algolia.instantsearch.android.highlighting.toSpannedString
import com.algolia.instantsearch.examples.android.databinding.ListItemLargeBinding

class MovieViewHolder(private val binding: ListItemLargeBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: Movie) {
        binding.itemTitle.text = TextUtils.concat(movie.highlightedTitle?.toSpannedString(), " (${movie.year})")
        binding.itemSubtitle.text = movie.highlightedGenres?.toSpannedString()
            ?: buildSpannedString { italic { append("unknown genre") } }
        binding.itemImage.load(movie.image) {
            placeholder(android.R.drawable.ic_media_play)
            error(android.R.drawable.ic_media_play)
            scale(Scale.FIT)
        }
    }
}
