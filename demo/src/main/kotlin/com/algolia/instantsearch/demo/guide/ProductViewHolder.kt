package com.algolia.instantsearch.demo.guide

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.helper.android.highlighting.toSpannedString
import kotlinx.android.synthetic.main.product_item.view.*


class ProductViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(product: Product) {
        view.productName.text = product.highlightedName?.toSpannedString() ?: product.name
    }
}