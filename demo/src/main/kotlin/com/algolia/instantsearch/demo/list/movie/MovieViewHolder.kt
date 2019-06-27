package com.algolia.instantsearch.demo.list.movie

import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.helper.highlighting.toSpannedString
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_item_large.view.*


class MovieViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(movie: Movie) {
        //FIXME: Is there a way we can still use String.format?
        //view.itemTitle.text = view.resources.getString(R.string.template_title).format(title, movie.year)

        view.itemTitle.text = TextUtils.concat(movie.highlightedTitle.toSpannedString(), " (${movie.year})")
        view.itemSubtitle.text = movie.highlightedGenres?.toSpannedString()
        Glide.with(view)
            .load(movie.image).placeholder(android.R.drawable.ic_media_play)
            .centerCrop()
            .into(view.itemImage)
    }
}