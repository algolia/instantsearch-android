package com.algolia.instantsearch.demo.selectable.numeric

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.inflate
import com.algolia.search.model.filter.Filter
import selectable.filter.SelectableFilterPresenter
import selectable.numeric.SelectableNumeric
import selectable.numeric.SelectableNumericsView


class SelectableNumericsAdapter :
    ListAdapter<SelectableNumeric, SelectableNumericViewHolder>(diffUtil),
    SelectableNumericsView {

    override var onClick: ((Filter.Numeric) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectableNumericViewHolder {
        return SelectableNumericViewHolder(parent.inflate(R.layout.selectable_item))
    }

    override fun onBindViewHolder(holder: SelectableNumericViewHolder, position: Int) {
        val (filter, selected) = getItem(position)

        holder.bind(SelectableFilterPresenter(filter), selected, View.OnClickListener { onClick?.invoke(filter) })
    }

    override fun setSelectableItems(selectableItems: List<SelectableNumeric>) {
        submitList(selectableItems)
    }

    companion object {

        private val diffUtil = object : DiffUtil.ItemCallback<SelectableNumeric>() {

            override fun areItemsTheSame(
                oldItem: SelectableNumeric,
                newItem: SelectableNumeric
            ): Boolean {
                return oldItem::class == newItem::class
            }

            override fun areContentsTheSame(
                oldItem: SelectableNumeric,
                newItem: SelectableNumeric
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}