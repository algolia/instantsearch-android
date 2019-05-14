package com.algolia.instantsearch.demo.widget

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.algolia.search.model.response.ResponseSearch
import kotlinx.android.synthetic.main.list_item.view.*
import kotlinx.serialization.json.content

class HitViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(hit: ResponseSearch.Hit) {
        view.itemName.text = hit["name"]?.content
    }
}