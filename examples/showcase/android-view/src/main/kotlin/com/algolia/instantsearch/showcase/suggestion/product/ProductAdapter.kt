package com.algolia.instantsearch.showcase.suggestion.product

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.android.highlighting.toSpannedString
import com.algolia.instantsearch.android.inflate
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.showcase.R
import com.algolia.instantsearch.showcase.suggestion.product.ProductAdapter.ProductViewHolder
import com.bumptech.glide.Glide

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
            Glide
                .with(view.context)
                .load(item.images.first())
                .into(view.findViewById(R.id.itemImage))
        }
    }

    private object ProductDiffUtil : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product) =
            oldItem.objectID == newItem.objectID

        override fun areContentsTheSame(oldItem: Product, newItem: Product) = oldItem == newItem
    }
}
