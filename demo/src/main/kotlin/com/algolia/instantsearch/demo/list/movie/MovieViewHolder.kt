package com.algolia.instantsearch.demo.list.movie

import android.view.View
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_item_large.view.*


class MovieViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(movie: Movie) {
        val title = buildSpannedString {
            val parts = movie.titleHighlight?.parts

            parts?.forEach { (isHighlighted, part) ->
                if (isHighlighted) {
                    println("High part: $part")
                    bold { append(part) }
                } else {
                    println("Normal part: $part")
                    append(part)
                }
            } ?: append(movie.title)
            append("(${movie.year})")
        }

        //TODO: Move to own demo, not paging's
        //FIXME: Is there a way we can still use String.format?
        //view.itemTitle.text = view.resources.getString(R.string.template_title).format(title, movie.year)
        view.itemTitle.text = title
        view.itemSubtitle.text = movie.genre.sorted().joinToString { it }
        Glide.with(view)
            .load(movie.image).placeholder(android.R.drawable.ic_media_play)
            .centerCrop()
            .into(view.itemImage)
    }
}