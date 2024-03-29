package com.algolia.instantsearch.examples.android.showcase.androidview.filter.list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.selectable.list.SelectableItem
import com.algolia.instantsearch.filter.DefaultFilterPresenter
import com.algolia.instantsearch.filter.list.FilterListView
import com.algolia.instantsearch.examples.android.databinding.ListItemSelectableBinding
import com.algolia.instantsearch.examples.android.showcase.androidview.layoutInflater
import com.algolia.search.model.filter.Filter

class FilterListAdapter<T : Filter> :
    ListAdapter<SelectableItem<T>, FilterListViewHolder>(DiffUtilItem()),
    FilterListView<T> {

    override var onSelection: Callback<T>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterListViewHolder {
        return FilterListViewHolder(
            ListItemSelectableBinding.inflate(parent.layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FilterListViewHolder, position: Int) {
        val (filter, selected) = getItem(position)

        holder.bind(DefaultFilterPresenter()(filter), selected) { onSelection?.invoke(filter) }
    }

    override fun setItems(items: List<SelectableItem<T>>) {
        submitList(items)
    }

    private class DiffUtilItem<T : Filter> : DiffUtil.ItemCallback<SelectableItem<T>>() {

        override fun areItemsTheSame(
            oldItem: SelectableItem<T>,
            newItem: SelectableItem<T>
        ): Boolean {
            return oldItem::class == newItem::class
        }

        override fun areContentsTheSame(
            oldItem: SelectableItem<T>,
            newItem: SelectableItem<T>
        ): Boolean {
            return oldItem == newItem
        }
    }
}
