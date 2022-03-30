package com.algolia.instantsearch.guides.places

import android.text.SpannedString
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.android.highlighting.toSpannedString
import com.algolia.instantsearch.android.inflate
import com.algolia.instantsearch.core.highlighting.HighlightTokenizer
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.guides.R
import com.algolia.search.model.places.PlaceLanguage
import com.algolia.search.model.search.HighlightResult
import com.algolia.search.serialize.toHighlights

class Adapter : ListAdapter<PlaceLanguage, Adapter.ViewHolder>(Adapter), HitsView<PlaceLanguage> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.list_item_small))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun setHits(hits: List<PlaceLanguage>) {
        submitList(hits)
    }

    class ViewHolder(val view: View) :
        RecyclerView.ViewHolder(view) {

        fun bind(place: PlaceLanguage) {
            val name = place.highlightResultOrNull
                ?.toHighlights("locale_names")
                ?.firstOrNull()
                ?.tokenize() ?: place.localNames.first()
            val county = place.highlightResultOrNull
                ?.toHighlights("county")
                ?.firstOrNull()
                ?.tokenize() ?: place.county.first()
            val postCode = place.postCodeOrNull?.firstOrNull()?.let { ", $it" } ?: ""

            view.findViewById<TextView>(R.id.itemName).text =
                TextUtils.concat(name, ", ", county, postCode)
        }

        private fun HighlightResult.tokenize(): SpannedString {
            return HighlightTokenizer()(value).toSpannedString()
        }
    }

    companion object : DiffUtil.ItemCallback<PlaceLanguage>() {
        override fun areItemsTheSame(oldItem: PlaceLanguage, newItem: PlaceLanguage) =
            oldItem::class == newItem::class

        override fun areContentsTheSame(oldItem: PlaceLanguage, newItem: PlaceLanguage) =
            oldItem == newItem
    }
}