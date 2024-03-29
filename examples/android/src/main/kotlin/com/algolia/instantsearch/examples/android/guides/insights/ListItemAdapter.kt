package com.algolia.instantsearch.examples.android.guides.insights

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.algolia.instantsearch.android.inflate
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.examples.android.R
import com.algolia.instantsearch.examples.android.guides.extension.ProductDiffUtil
import com.algolia.instantsearch.examples.android.guides.insights.ListItemAdapter.ListItemViewHolder
import com.algolia.instantsearch.examples.android.guides.model.Product
import com.algolia.instantsearch.tracker.HitsTracker

class ListItemAdapter(private val hitsTracker: HitsTracker) :
    ListAdapter<Product, ListItemViewHolder>(ProductDiffUtil),
    HitsView<Product> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        return ListItemViewHolder(hitsTracker, parent.inflate(R.layout.list_item_large_buy))
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, position) }
    }

    override fun setHits(hits: List<Product>) {
        submitList(hits)
    }

    class ListItemViewHolder(private val hitsTracker: HitsTracker, val view: View) :
        RecyclerView.ViewHolder(view) {

        fun bind(model: Product, position: Int) {
            view.findViewById<TextView>(R.id.itemTitle).text = model.name
            view.findViewById<TextView>(R.id.itemSubtitle).text = model.description
            view.findViewById<Button>(R.id.buy).setOnClickListener { hitsTracker.trackConvert(model) }
            // The first object in the list of search results has a position of 1 (not zero)
            itemView.setOnClickListener { hitsTracker.trackClick(model, position + 1) }
            view.findViewById<ImageView>(R.id.itemImage).load(model.image) { crossfade(true) }
        }
    }
}
