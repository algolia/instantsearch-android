package com.algolia.instantsearch.demo.list.movie

import android.text.TextUtils
import android.view.View
import androidx.core.text.buildSpannedString
import androidx.core.text.italic
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.helper.android.highlighting.toSpannedString
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_item_large.view.*


class MovieViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(movie: Movie) {
        view.itemTitle.text = TextUtils.concat(movie.highlightedTitle?.toSpannedString(), " (${movie.year})")
        view.itemSubtitle.text = movie.highlightedGenres?.toSpannedString()
            ?: buildSpannedString { italic { append("unknown genre") } }
        Glide.with(view)
            .load(movie.image).placeholder(android.R.drawable.ic_media_play)
            .centerCrop()
            .into(view.itemImage)
    }
}