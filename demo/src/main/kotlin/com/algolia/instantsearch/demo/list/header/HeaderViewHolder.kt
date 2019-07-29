package com.algolia.instantsearch.demo.list.header

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class HeaderViewHolder(val view: TextView) : RecyclerView.ViewHolder(view) {

    fun bind(string: String) {
        view.text = string
    }
}