package com.algolia.instantsearch.example.wearos

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.algolia.instantsearch.android.highlighting.toSpannedString
import com.algolia.instantsearch.android.inflate
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.example.wearos.ShowAdapter.ShowViewHolder

internal class ShowAdapter : ListAdapter<Show, ShowViewHolder>(ShowDiffUtil), HitsView<Show> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        return ShowViewHolder(parent.inflate(R.layout.list_item))
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) holder.bind(item)
    }

    override fun setHits(hits: List<Show>) {
        submitList(hits)
    }

    class ShowViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(show: Show) {
            view.findViewById<TextView>(R.id.show_title).text = show.highlightedTitle?.toSpannedString()
            view.findViewById<ImageView>(R.id.show_poster).load(show.posterUrl) {
                placeholder(android.R.drawable.ic_media_play)
                transformations(RoundedCornersTransformation(), GrayscaleTransformation(0.25f))
                scale(Scale.FILL)
                error(android.R.drawable.ic_media_play)
            }
        }
    }

    object ShowDiffUtil : DiffUtil.ItemCallback<Show>() {

        override fun areItemsTheSame(oldItem: Show, newItem: Show): Boolean {
            return oldItem.objectID == newItem.objectID
        }

        override fun areContentsTheSame(oldItem: Show, newItem: Show): Boolean {
            return oldItem == newItem
        }
    }
}
