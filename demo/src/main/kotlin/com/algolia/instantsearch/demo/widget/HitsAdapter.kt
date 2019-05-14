package com.algolia.instantsearch.demo.widget

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.inflate
import com.algolia.search.model.response.ResponseSearch.Hit
import com.algolia.search.serialize.KeyObjectID

class HitsAdapter : PagedListAdapter<Hit, HitViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HitViewHolder =
        HitViewHolder(parent.inflate(R.layout.list_item))


    override fun onBindViewHolder(holder: HitViewHolder, position: Int) {
        val hit = getItem(position)
        hit?.let { holder.bind(it) }
    }


    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<Hit>() {
            override fun areItemsTheSame(oldItem: Hit, newItem: Hit): Boolean {
                return oldItem[KeyObjectID] == newItem[KeyObjectID]
            }

            override fun areContentsTheSame(oldItem: Hit, newItem: Hit): Boolean {
                return oldItem == newItem
            }
        }
    }
}