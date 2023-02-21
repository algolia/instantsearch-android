package com.algolia.instantsearch.examples.android.guides.querysuggestion

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.algolia.instantsearch.android.highlighting.toSpannedString
import com.algolia.instantsearch.android.inflate
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.examples.android.R
import com.algolia.instantsearch.examples.android.guides.querysuggestion.ProductAdapter.ProductViewHolder

class ProductAdapter : ListAdapter<Product, ProductViewHolder>(ProductDiffUtil), HitsView<Product> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ProductViewHolder(parent.inflate(R.layout.list_item_large))

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) =
        holder.bind(getItem(position))

    override fun setHits(hits: List<Product>) = submitList(hits)

    class ProductViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: Product) {
            view.findViewById<TextView>(R.id.itemTitle).text =
                item.highlightedName?.toSpannedString() ?: item.name
            view.findViewById<TextView>(R.id.itemSubtitle).text = item.price.value
            view.findViewById<ImageView>(R.id.itemImage).load(item.images.first())
        }
    }

    private object ProductDiffUtil : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product) = oldItem.objectID == newItem.objectID
        override fun areContentsTheSame(oldItem: Product, newItem: Product) = oldItem == newItem
    }
}
