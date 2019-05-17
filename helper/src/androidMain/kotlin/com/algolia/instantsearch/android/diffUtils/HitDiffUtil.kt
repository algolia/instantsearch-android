package com.algolia.instantsearch.android.diffUtils

import androidx.recyclerview.widget.DiffUtil
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.serialize.KeyObjectID

object HitDiffUtil : DiffUtil.ItemCallback<ResponseSearch.Hit>() {

    override fun areItemsTheSame(oldItem: ResponseSearch.Hit, newItem: ResponseSearch.Hit): Boolean {
        return oldItem[KeyObjectID] == newItem[KeyObjectID]
    }

    override fun areContentsTheSame(oldItem: ResponseSearch.Hit, newItem: ResponseSearch.Hit): Boolean {
        return oldItem == newItem
    }
}