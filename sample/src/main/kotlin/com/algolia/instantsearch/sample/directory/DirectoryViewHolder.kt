package com.algolia.instantsearch.sample.directory

import android.content.Intent
import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item.view.*


sealed class DirectoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    data class Header(val view: TextView) : DirectoryViewHolder(view) {

        fun bind(item: DirectoryItem.Header) {
            view.text = item.name.capitalize()
        }
    }

    data class Item(val view: View) : DirectoryViewHolder(view) {

        fun bind(item: DirectoryItem.Item) {
            val type = DirectoryType.values()[item.hit.objectID.raw.toInt()]
            val text = item.hit.highlightResults?.getValue("name")?.let {
                Html.fromHtml(it.value, Html.FROM_HTML_MODE_COMPACT)
            } ?: item.hit.name

            view.itemName.text = text
            view.setOnClickListener { view.context.startActivity(Intent(view.context, type.clazz.java)) }
        }
    }
}