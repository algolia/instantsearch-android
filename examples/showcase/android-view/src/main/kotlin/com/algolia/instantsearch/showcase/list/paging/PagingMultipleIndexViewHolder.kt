package com.algolia.instantsearch.showcase.list.paging

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.android.list.autoScrollToStart
import com.algolia.instantsearch.showcase.databinding.HeaderItemBinding


sealed class PagingMultipleIndexViewHolder(
    view: View
) : RecyclerView.ViewHolder(view) {

    class Header(
        val binding: HeaderItemBinding
    ) : PagingMultipleIndexViewHolder(binding.root) {

        fun bind(item: PagingMultipleIndexItem.Header) {
            binding.root.text = item.name
        }
    }

    class Movies(
        val view: RecyclerView
    ) : PagingMultipleIndexViewHolder(view) {

        fun bind(item: PagingMultipleIndexItem.Movies) {
            view.let {
                it.adapter = item.adapter
                it.layoutManager = LinearLayoutManager(view.context, RecyclerView.HORIZONTAL, false)
                it.autoScrollToStart(item.adapter)
            }
        }
    }

    class Actors(
        val view: RecyclerView
    ) : PagingMultipleIndexViewHolder(view) {

        fun bind(item: PagingMultipleIndexItem.Actors) {
            view.let {
                it.autoScrollToStart(item.adapter)
                it.adapter = item.adapter
                it.layoutManager = LinearLayoutManager(view.context, RecyclerView.HORIZONTAL, false)
            }
        }
    }
}
