package com.algolia.instantsearch.demo.hierarchical

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.demo.dip
import com.algolia.instantsearch.helper.hierarchical.HierarchicalItem
import kotlinx.android.synthetic.main.list_item_selectable.view.*

class HierarchicalViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(item: HierarchicalItem, onClick: View.OnClickListener) {
        view.elevation = ((6 - item.level * 2) * view.context.dip(4)).toFloat()
        view.selectableItemName.text = item.displayName
        view.selectableItemSubtitle.text = item.facet.count.toString()
        view.selectableItemSubtitle.visibility = View.VISIBLE
        view.setOnClickListener(onClick)
    }
}