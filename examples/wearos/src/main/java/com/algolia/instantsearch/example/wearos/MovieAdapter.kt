package com.algolia.instantsearch.example.wearos

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.algolia.instantsearch.android.inflate
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.example.wearos.MovieAdapter.MovieViewHolder

class MovieAdapter : ListAdapter<Movie, MovieViewHolder>(MovieDiffUtil), HitsView<Movie> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(parent.inflate(R.layout.list_item))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) holder.bind(item)
    }

    override fun setHits(hits: List<Movie>) {
        submitList(hits)
    }

    class MovieViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(movie: Movie) {
            //view.findViewById<TextView>(R.id.movie_title).text =
            //    TextUtils.concat(movie.highlightedTitle?.toSpannedString(), " (${movie.year})")

            view.findViewById<ImageView>(R.id.movie_image).load(movie.posterUrl) {
                placeholder(android.R.drawable.ic_media_play)
                transformations(RoundedCornersTransformation())
                scale(Scale.FILL)
                error(android.R.drawable.ic_media_play)
            }
        }
    }

    object MovieDiffUtil : DiffUtil.ItemCallback<Movie>() {

        override fun areItemsTheSame(
            oldItem: Movie,
            newItem: Movie
        ): Boolean {
            return oldItem.objectID == newItem.objectID
        }

        override fun areContentsTheSame(
            oldItem: Movie,
            newItem: Movie
        ): Boolean {
            return oldItem == newItem
        }
    }
}
