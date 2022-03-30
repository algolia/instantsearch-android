package com.algolia.instantsearch.showcase.list.movie

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.algolia.instantsearch.showcase.databinding.ListItemMovieBinding
import com.algolia.instantsearch.showcase.dip
import com.algolia.instantsearch.showcase.layoutInflater

class MovieAdapterNested : PagingDataAdapter<Movie, MovieViewHolderNested>(MovieDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolderNested {
        val dip8 = parent.context.dip(8)
        val layoutParams =
            ViewGroup.MarginLayoutParams(parent.width / 2, ViewGroup.LayoutParams.MATCH_PARENT)
                .apply {
                    setMargins(dip8, dip8, dip8, dip8)
                }
        val binding = ListItemMovieBinding.inflate(parent.layoutInflater, parent, false).apply {
            root.layoutParams = layoutParams
        }
        return MovieViewHolderNested(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolderNested, position: Int) {
        val item = getItem(position)
        if (item != null) holder.bind(item)
    }
}
