package com.algolia.instantsearch.showcase.directory

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.android.highlighting.toSpannedString
import com.algolia.instantsearch.showcase.databinding.HeaderItemBinding
import com.algolia.instantsearch.showcase.databinding.ListItemSmallBinding
import com.algolia.search.serialize.KeyIndexName
import com.algolia.search.serialize.KeyName

sealed class DirectoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    data class Header(private val binding: HeaderItemBinding) : DirectoryViewHolder(binding.root) {

        fun bind(item: DirectoryItem.Header) {
            binding.root.text = item.name
        }
    }

    data class Item(private val binding: ListItemSmallBinding) : DirectoryViewHolder(binding.root) {

        fun bind(item: DirectoryItem.Item) {
            val text = item.hit.highlightedName?.toSpannedString() ?: item.hit.name
            val view = binding.root

            binding.itemName.text = text
            view.setOnClickListener {
                val showcase = showcases.getValue(item.hit.objectID).java
                val intent = Intent(view.context, showcase).apply {
                    putExtra(KeyIndexName, item.hit.index)
                    putExtra(KeyName, item.hit.name)
                }

                view.context.startActivity(intent)
            }
        }
    }
}
