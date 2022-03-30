package com.algolia.instantsearch.showcase.hierarchical

import android.graphics.Typeface
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.hierarchical.HierarchicalItem
import com.algolia.instantsearch.showcase.databinding.ListItemSelectableBinding
import com.algolia.instantsearch.showcase.dip

class HierarchicalViewHolder(private val binding: ListItemSelectableBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: HierarchicalItem, onClick: View.OnClickListener) {
        binding.root.elevation = ((6 - item.level * 2) * binding.root.context.dip(4)).toFloat()
        binding.root.setOnClickListener(onClick)
        binding.selectableItemName.text = item.displayName
        binding.selectableItemSubtitle.text = item.facet.count.toString()
        binding.selectableItemSubtitle.visibility = View.VISIBLE
        if (item.isSelected) selected() else unselected()
    }

    private fun selected() {
        binding.selectableItemIcon.visibility = View.VISIBLE
        binding.selectableItemName.setTypeface(null, Typeface.BOLD)
    }

    private fun unselected() {
        binding.selectableItemIcon.visibility = View.INVISIBLE
        binding.selectableItemName.setTypeface(null, Typeface.NORMAL)
    }
}
