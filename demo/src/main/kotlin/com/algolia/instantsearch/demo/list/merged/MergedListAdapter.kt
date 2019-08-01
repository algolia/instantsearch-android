package com.algolia.instantsearch.demo.list.merged

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.list.actor.Actor
import com.algolia.instantsearch.demo.list.actor.ActorViewHolder
import com.algolia.instantsearch.demo.list.actor.ActorViewHolderSmall
import com.algolia.instantsearch.demo.list.header.HeaderViewHolder
import com.algolia.instantsearch.demo.list.movie.Movie
import com.algolia.instantsearch.demo.list.movie.MovieViewHolder
import com.algolia.instantsearch.helper.android.inflate


class MergedListAdapter : ListAdapter<Any, RecyclerView.ViewHolder>(this) {

    private enum class ViewType {
        Header,
        Movies,
        Actors
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (ViewType.values()[viewType]) {
            ViewType.Header -> HeaderViewHolder(parent.inflate<TextView>(R.layout.header_item))
            ViewType.Movies -> MovieViewHolder(parent.inflate(R.layout.list_item_large))
            ViewType.Actors -> ActorViewHolderSmall(parent.inflate(R.layout.list_item_small))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        when (holder) {
            is HeaderViewHolder -> holder.bind(item as String)
            is MovieViewHolder -> holder.bind(item as Movie)
            is ActorViewHolderSmall -> holder.bind(item as Actor)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is String -> ViewType.Header
            is Movie -> ViewType.Movies
            is Actor -> ViewType.Actors
            else -> throw Exception("Not implemented")
        }.ordinal
    }

    private companion object : DiffUtil.ItemCallback<Any>() {

        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return oldItem == newItem
        }
    }
}