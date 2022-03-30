package com.algolia.instantsearch.showcase.filter.facet.dynamic

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.algolia.instantsearch.android.filter.facet.dynamic.DynamicFacetListHeaderViewHolder
import com.algolia.instantsearch.android.filter.facet.dynamic.DynamicFacetListItemViewHolder
import com.algolia.instantsearch.android.filter.facet.dynamic.DynamicFacetListViewHolder
import com.algolia.instantsearch.android.filter.facet.dynamic.DynamicFacetListViewHolder.ViewType
import com.algolia.instantsearch.android.filter.facet.dynamic.DynamicFacetModel
import com.algolia.instantsearch.android.inflate
import com.algolia.instantsearch.showcase.R
import com.algolia.instantsearch.showcase.databinding.ListItemSelectableBinding
import com.algolia.instantsearch.showcase.layoutInflater

class ViewHolderFactory : DynamicFacetListViewHolder.Factory {

    override fun createViewHolder(
        parent: ViewGroup,
        viewType: ViewType
    ): DynamicFacetListViewHolder<out DynamicFacetModel> {
        return when (viewType) {
            ViewType.Header -> HeaderViewHolder(parent.inflate<TextView>(R.layout.header_item))
            ViewType.Item -> ItemViewHolder(
                ListItemSelectableBinding.inflate(parent.layoutInflater, parent, false)
            )
        }
    }

    class HeaderViewHolder(view: TextView) : DynamicFacetListHeaderViewHolder(view) {
        override fun bind(item: DynamicFacetModel.Header, onClick: View.OnClickListener?) {
            val textView = view as TextView
            textView.text = item.attribute.raw
        }
    }

    class ItemViewHolder(private val binding: ListItemSelectableBinding) :
        DynamicFacetListItemViewHolder(binding.root) {
        override fun bind(item: DynamicFacetModel.Item, onClick: View.OnClickListener?) {
            binding.selectableItemName.text = item.facet.value
            binding.selectableItemSubtitle.text = "${item.facet.count}"
            binding.selectableItemSubtitle.visibility = View.VISIBLE
            binding.selectableItemIcon.visibility = if (item.selected) View.VISIBLE else View.GONE
            binding.root.setOnClickListener(onClick)
        }
    }
}
