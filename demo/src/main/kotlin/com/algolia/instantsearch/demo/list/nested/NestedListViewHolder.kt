package com.algolia.instantsearch.demo.list.nested

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


sealed class NestedListViewHolder(
    view: View
) : RecyclerView.ViewHolder(view) {

    data class Header(
        val view: TextView
    ) : NestedListViewHolder(view) {

        fun bind(item: NestedListItem.Header) {
            view.text = item.name
        }
    }

    data class Movies(
        val view: RecyclerView
    ) : NestedListViewHolder(view) {

        fun bind(item: NestedListItem.Movies) {
            view.let {
                it.adapter = item.adapter
                it.layoutManager = LinearLayoutManager(view.context, RecyclerView.HORIZONTAL, false)
            }
        }
    }

    data class Actors(
        val view: RecyclerView
    ) : NestedListViewHolder(view) {

        fun bind(item: NestedListItem.Actors) {

            view.let {
                it.adapter = item.adapter
                it.layoutManager = LinearLayoutManager(view.context, RecyclerView.HORIZONTAL, false)
            }
        }
    }
}