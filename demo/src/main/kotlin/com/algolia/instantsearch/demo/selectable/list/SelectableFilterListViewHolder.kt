package com.algolia.instantsearch.demo.selectable.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.selectable_item.view.*


class SelectableFilterListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(text: String, selected: Boolean, onClickListener: View.OnClickListener) {
        view.setOnClickListener(onClickListener)
        view.selectableItemName.text = text
        view.selectableItemIcon.visibility = if (selected) View.VISIBLE else View.INVISIBLE
    }
}