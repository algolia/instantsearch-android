package com.algolia.instantsearch.demo.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.demo.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_item_large.view.*

class MovieViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(movie: Movie) {
        view.itemTitle.text = view.resources.getString(R.string.template_title).format(movie.title, movie.year)
        view.itemSubtitle.text = movie.genre.sorted().joinToString { it }

        Glide.with(view)
            .load(movie.image).placeholder(android.R.drawable.ic_media_play)
            .centerCrop()
            .into(view.itemImage)
    }
}