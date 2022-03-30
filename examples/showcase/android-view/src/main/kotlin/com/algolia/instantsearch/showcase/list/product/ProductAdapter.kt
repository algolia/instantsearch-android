package com.algolia.instantsearch.showcase.list.product

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.showcase.databinding.ListItemProductBinding
import com.algolia.instantsearch.showcase.layoutInflater
import com.bumptech.glide.Glide

class ProductAdapter : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffUtil),
    HitsView<Product> {

    var callback: ((Product) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ListItemProductBinding.inflate(parent.layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) holder.bind(item)
        holder.itemView.setOnClickListener {
            callback?.invoke(item)
        }
    }

    override fun setHits(hits: List<Product>) {
        submitList(hits)
    }

    class ProductViewHolder(private val binding: ListItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.itemName.text = product.name
            binding.itemBrand.text = product.brand
            binding.itemType.text = product.type
            Glide.with(binding.root)
                .load(product.image).placeholder(android.R.drawable.ic_media_play)
                .centerCrop()
                .into(binding.itemImage)
        }
    }

    object ProductDiffUtil : DiffUtil.ItemCallback<Product>() {

        override fun areItemsTheSame(
            oldItem: Product,
            newItem: Product
        ): Boolean {
            return oldItem.objectID == newItem.objectID
        }

        override fun areContentsTheSame(
            oldItem: Product,
            newItem: Product
        ): Boolean {
            return oldItem == newItem
        }
    }
}
