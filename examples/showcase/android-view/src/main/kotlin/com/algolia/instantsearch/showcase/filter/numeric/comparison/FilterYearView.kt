package com.algolia.instantsearch.showcase.filter.numeric.comparison

import android.view.inputmethod.EditorInfo
import com.algolia.instantsearch.core.number.Computation
import com.algolia.instantsearch.core.number.NumberView
import com.algolia.instantsearch.core.number.just
import com.algolia.instantsearch.showcase.databinding.ShowcaseFilterComparisonBinding
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.NumericOperator

class FilterYearView(
    val binding: ShowcaseFilterComparisonBinding,
    year: Attribute,
    operator: NumericOperator
) : NumberView<Int> {

    init {
        binding.inputText.text = "$year ${operator.raw}"
    }

    override fun setComputation(computation: Computation<Int>) {
        binding.inputEditText.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val year = v.text.toString().toIntOrNull()

                computation.just(year)
            }
            true
        }
    }

    override fun setText(text: String) {
        binding.inputEditText.setText(text)
    }
}
