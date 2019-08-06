package com.algolia.instantsearch.demo.list.paging

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.helper.android.list.autoScrollToStart


sealed class PagingMultipleIndexViewHolder(
    view: View
) : RecyclerView.ViewHolder(view) {

    data class Header(
        val view: TextView
    ) : PagingMultipleIndexViewHolder(view) {

        fun bind(item: PagingMultipleIndexItem.Header) {
            view.text = item.name
        }
    }

    data class Movies(
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

    data class Actors(
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