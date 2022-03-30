package com.algolia.instantsearch.guides.directory

import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.android.highlighting.toSpannedString
import com.algolia.instantsearch.guides.R
import com.algolia.search.serialize.KeyName

sealed class DirectoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    data class Header(val view: TextView) : DirectoryViewHolder(view) {

        fun bind(item: DirectoryItem.Header) {
            view.text = item.name
        }
    }

    data class Item(val view: View) : DirectoryViewHolder(view) {

        fun bind(item: DirectoryItem.Item) {
            val text = item.hit.highlightedName?.toSpannedString() ?: item.hit.name
            view.findViewById<TextView>(R.id.itemName).text = text

            view.setOnClickListener {
                val intent = Intent(view.context, guides.getValue(item.hit.objectID).java).apply {
                    putExtra(KeyName, item.hit.name)
                }

                view.context.startActivity(intent)
            }
        }
    }
}
