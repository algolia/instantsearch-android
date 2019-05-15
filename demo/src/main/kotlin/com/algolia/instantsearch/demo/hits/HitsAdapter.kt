package com.algolia.instantsearch.demo.hits

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.inflate
import com.algolia.search.model.response.ResponseSearch.Hit


class HitsAdapter : PagedListAdapter<Hit, HitViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HitViewHolder =
        HitViewHolder(parent.inflate(R.layout.list_item_paging))


    override fun onBindViewHolder(holder: HitViewHolder, position: Int) {
        val item = getItem(position)

        if (item != null) holder.bind(item)
    }

    companion object {

        private val diffUtil = object : DiffUtil.ItemCallback<Hit>() {

            override fun areItemsTheSame(oldItem: Hit, newItem: Hit): Boolean {
                return oldItem::class == newItem::class
            }

            override fun areContentsTheSame(oldItem: Hit, newItem: Hit): Boolean {
                return oldItem == newItem
            }
        }
    }
}