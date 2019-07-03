package com.algolia.instantsearch.demo.highlighting

import android.graphics.Color
import android.text.TextUtils
import android.text.style.BackgroundColorSpan
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.buildSpannedString
import androidx.core.text.italic
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.inflate
import com.algolia.instantsearch.demo.list.movie.Movie
import com.algolia.instantsearch.demo.list.movie.MovieDiffUtil
import com.algolia.instantsearch.helper.highlighting.toSpannedString
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_item_highlighting.view.*


class MovieAdapterHighlighted : ListAdapter<Movie, MovieAdapterHighlighted.MovieHighlightedViewHolder>(MovieDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHighlightedViewHolder {
        return MovieHighlightedViewHolder(parent.inflate(R.layout.list_item_highlighting))
    }

    override fun onBindViewHolder(holder: MovieHighlightedViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    class MovieHighlightedViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(movie: Movie) {
            movie.bindTitleTo(view.itemTitle)
            movie.bindSubtitleTo(view.itemSubtitle)
            movie.bindFooterTo(view.itemFooter)
            movie.bindImageTo(view.itemImage)
        }

        private fun Movie.bindTitleTo(view: TextView) {
            view.text = TextUtils.concat(highlightedTitle?.toSpannedString(), " ($year)")
        }

        private fun Movie.bindSubtitleTo(view: TextView) {
            view.text = highlightedGenres?.toSpannedString(BackgroundColorSpan(Color.YELLOW))
                ?: buildSpannedString { italic { append("unknown genre") } }
        }

        private fun Movie.bindFooterTo(view: TextView) {
            view.text = highlightedActors?.let { list ->
                list.sortedByDescending { it.highlightedTokens.size }.joinToString { highlight ->
                    highlight.tokens.joinToString("") {
                        if (it.highlighted) it.content.toUpperCase() else it.content
                    }
                }
            }
        }

        private fun Movie.bindImageTo(view: ImageView) {
            Glide.with(view)
                .load(image).placeholder(android.R.drawable.ic_media_play)
                .centerCrop()
                .into(view)
        }
    }
}