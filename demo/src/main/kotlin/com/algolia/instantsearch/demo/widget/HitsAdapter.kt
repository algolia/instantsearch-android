package com.algolia.instantsearch.demo.widget

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.algolia.instantsearch.core.HitsView
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.inflate
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.serialize.KeyObjectID

//TODO: refactor using PagedListAdapter, see S&W's AdapterListPaged
class HitsAdapter : ListAdapter<ResponseSearch.Hit, HitViewHolder>(diffUtil), HitsView<ResponseSearch.Hit> {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HitViewHolder =
        HitViewHolder(parent.inflate(R.layout.list_item))

    override fun onBindViewHolder(holder: HitViewHolder, position: Int) = holder.bind(getItem(position))

    override fun setHits(hits: List<ResponseSearch.Hit>) = submitList(hits)

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<ResponseSearch.Hit>() {
            override fun areItemsTheSame(oldItem: ResponseSearch.Hit, newItem: ResponseSearch.Hit): Boolean {
                return oldItem[KeyObjectID] == newItem[KeyObjectID]
            }

            override fun areContentsTheSame(oldItem: ResponseSearch.Hit, newItem: ResponseSearch.Hit): Boolean {
                return oldItem == newItem
            }
        }
    }
}