package com.algolia.instantsearch.demo.home

import android.content.Intent
import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.algolia.search.serialize.KeyIndexName
import com.algolia.search.serialize.KeyName
import kotlinx.android.synthetic.main.list_item_small.view.*


sealed class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    data class Header(val view: TextView) : HomeViewHolder(view) {

        fun bind(item: HomeItem.Header) {
            view.text = item.name
        }
    }

    data class Item(val view: View) : HomeViewHolder(view) {

        fun bind(item: HomeItem.Item) {
            val text = item.hit.highlightResults?.getValue("name")?.let {
                Html.fromHtml(it.value, Html.FROM_HTML_MODE_COMPACT)
            } ?: item.hit.name

            view.itemName.text = text
            view.setOnClickListener {
                val intent = Intent(view.context, homeActivities.getValue(item.hit.objectID).java).apply {
                    putExtra(KeyIndexName, item.hit.index)
                    putExtra(KeyName, item.hit.name)
                }

                view.context.startActivity(intent)
            }
        }
    }
}