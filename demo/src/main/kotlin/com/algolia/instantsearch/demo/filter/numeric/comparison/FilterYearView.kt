package com.algolia.instantsearch.demo.filter.numeric.comparison

import android.view.View
import android.view.inputmethod.EditorInfo
import com.algolia.instantsearch.core.selectable.number.Computation
import com.algolia.instantsearch.core.selectable.number.SelectableNumberView
import com.algolia.instantsearch.core.selectable.number.just
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.NumericOperator
import kotlinx.android.synthetic.main.demo_filter_comparison.view.*


class FilterYearView(
    val view: View,
    val year: Attribute,
    val operator: NumericOperator
) : SelectableNumberView<Int> {

    init {
        view.inputText.text = "$year ${operator.raw}"
    }

    override fun setComputation(computation: Computation<Int>) {
        view.inputEditText.setOnEditorActionListener { v, actionId, _ ->
            if (actionId ==  EditorInfo.IME_ACTION_DONE) {
                val year = v.text.toString().toIntOrNull()

                computation.just(year)
            }
            true
        }
    }

    override fun setItem(item: Int?) {
        if (item != null) {
            view.inputEditText.setText(item.toString())
        } else view.inputEditText.text?.clear()
    }
}