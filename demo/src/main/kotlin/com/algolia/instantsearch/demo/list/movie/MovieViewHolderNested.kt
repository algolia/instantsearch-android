package com.algolia.instantsearch.demo.list.movie

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_item_movie.view.*


class MovieViewHolderNested(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(movie: Movie) {
        view.itemTitle.text = movie.title
        view.itemSubtitle.text = movie.genre.sorted().joinToString { it }
        view.itemCaption.text = movie.year
        Glide.with(view)
            .load(movie.image).placeholder(android.R.drawable.ic_media_play)
            .centerCrop()
            .into(view.itemImage)
    }
}