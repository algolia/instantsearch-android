package com.algolia.instantsearch.demo.hits

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.algolia.search.model.response.ResponseSearch
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_item_paging.view.*
import kotlinx.serialization.json.content


class HitViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(hit: ResponseSearch.Hit) {
        view.itemTitle.text = "${hit["title"]?.content}"
        view.itemYear.text = "${hit["year"]?.content}"
        val imageURL = hit["image"]?.content
        imageURL?.let {
            Glide.with(view)
                .load(imageURL).placeholder(android.R.drawable.ic_media_play)
                .into(view.itemImage)
        }
    }
}