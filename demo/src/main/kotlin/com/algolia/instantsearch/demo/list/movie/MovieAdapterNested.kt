package com.algolia.instantsearch.demo.list.movie

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.dip
import com.algolia.instantsearch.helper.android.inflate


class MovieAdapterNested : PagedListAdapter<Movie, MovieViewHolderNested>(MovieDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolderNested {
        val dip8 = parent.context.dip(8)
        val layoutParams = ViewGroup.MarginLayoutParams(parent.width / 2, ViewGroup.LayoutParams.MATCH_PARENT).apply {
            setMargins(dip8, dip8, dip8, dip8)
        }
        val view = parent.inflate(R.layout.list_item_movie).also {
            it.layoutParams = layoutParams
        }
        return MovieViewHolderNested(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolderNested, position: Int) {
        val item = getItem(position)

        if (item != null) holder.bind(item)
    }
}