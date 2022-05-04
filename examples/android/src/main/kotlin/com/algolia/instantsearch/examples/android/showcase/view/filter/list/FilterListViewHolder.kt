package com.algolia.instantsearch.examples.android.showcase.view.filter.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.examples.android.databinding.ListItemSelectableBinding

class FilterListViewHolder(val binding: ListItemSelectableBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(text: String, selected: Boolean, onClickListener: View.OnClickListener) {
        binding.root.setOnClickListener(onClickListener)
        binding.selectableItemName.text = text
        binding.selectableItemIcon.visibility = if (selected) View.VISIBLE else View.INVISIBLE
    }
}
