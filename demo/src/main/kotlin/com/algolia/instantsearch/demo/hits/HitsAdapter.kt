package com.algolia.instantsearch.demo.hits

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.algolia.instantsearch.android.diffUtils.HitDiffUtil
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.inflate
import com.algolia.search.model.response.ResponseSearch.Hit


class HitsAdapter : PagedListAdapter<Hit, HitViewHolder>(HitDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HitViewHolder =
        HitViewHolder(parent.inflate(R.layout.list_item_paging))


    override fun onBindViewHolder(holder: HitViewHolder, position: Int) {
        val item = getItem(position)

        if (item != null) holder.bind(item)
    }
}