package com.algolia.instantsearch.samples.guides.gettingstarted

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.android.highlighting.toSpannedString
import com.algolia.instantsearch.android.inflate
import com.algolia.instantsearch.samples.R
import com.algolia.instantsearch.samples.guides.extension.ProductDiffUtil
import com.algolia.instantsearch.samples.guides.gettingstarted.ProductAdapter.ProductViewHolder
import com.algolia.instantsearch.samples.guides.model.Product

class ProductAdapter : PagingDataAdapter<Product, ProductViewHolder>(ProductDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(parent.inflate(R.layout.list_item_small))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    class ProductViewHolder(val view: View) :
        RecyclerView.ViewHolder(view) {

        fun bind(product: Product) {
            view.findViewById<TextView>(R.id.itemName).text =
                product.highlightedName?.toSpannedString() ?: product.name
        }
    }
}
