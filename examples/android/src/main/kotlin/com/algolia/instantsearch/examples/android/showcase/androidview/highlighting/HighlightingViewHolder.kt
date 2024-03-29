package com.algolia.instantsearch.examples.android.showcase.androidview.highlighting

import android.graphics.Color
import android.text.TextUtils
import android.text.style.BackgroundColorSpan
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.buildSpannedString
import androidx.core.text.italic
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.algolia.instantsearch.android.highlighting.toSpannedString
import com.algolia.instantsearch.examples.android.databinding.ListItemHighlightingBinding
import com.algolia.instantsearch.examples.android.showcase.androidview.list.movie.Movie

class HighlightingViewHolder(private val binding: ListItemHighlightingBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: Movie) {
        movie.bindTitleTo(binding.itemTitle)
        movie.bindSubtitleTo(binding.itemSubtitle)
        movie.bindFooterTo(binding.itemFooter)
        movie.bindImageTo(binding.itemImage)
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
                    if (it.highlighted) it.content.uppercase() else it.content
                }
            }
        }
    }

    private fun Movie.bindImageTo(view: ImageView) {
        view.load(image) {
            placeholder(android.R.drawable.ic_media_play)
            error(android.R.drawable.ic_media_play)
        }
    }
}
