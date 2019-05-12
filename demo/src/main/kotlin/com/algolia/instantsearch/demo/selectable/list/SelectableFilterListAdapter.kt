package com.algolia.instantsearch.demo.selectable.list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.inflate
import com.algolia.search.model.filter.Filter
import selectable.filter.SelectableFilterPresenter
import selectable.list.SelectableFilterListView
import selectable.list.SelectableItem


class SelectableFilterListAdapter<T: Filter> :
    ListAdapter<SelectableItem<T>, SelectableFilterListViewHolder>(DiffUtilItem()),
    SelectableFilterListView<T> {

    override var onClick: ((T) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectableFilterListViewHolder {
        return SelectableFilterListViewHolder(parent.inflate(R.layout.selectable_item))
    }

    override fun onBindViewHolder(holder: SelectableFilterListViewHolder, position: Int) {
        val (filter, selected) = getItem(position)

        holder.bind(SelectableFilterPresenter(filter), selected, View.OnClickListener { onClick?.invoke(filter) })
    }

    override fun setSelectableItems(selectableItems: List<SelectableItem<T>>) {
        submitList(selectableItems)
    }

    private class DiffUtilItem<T: Filter>: DiffUtil.ItemCallback<SelectableItem<T>>() {

        override fun areItemsTheSame(oldItem: SelectableItem<T>, newItem: SelectableItem<T>): Boolean {
            return oldItem::class == newItem::class
        }

        override fun areContentsTheSame(oldItem: SelectableItem<T>, newItem: SelectableItem<T>): Boolean {
            return oldItem == newItem
        }
    }
}