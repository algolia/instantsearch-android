package com.algolia.instantsearch.showcase.list.header

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HeaderViewHolder(val view: TextView) : RecyclerView.ViewHolder(view) {

    fun bind(string: String) {
        view.text = string
    }
}
