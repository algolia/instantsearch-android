package com.algolia.instantsearch.showcase.filter.numeric.comparison

import com.algolia.instantsearch.core.number.Computation
import com.algolia.instantsearch.core.number.NumberView
import com.algolia.instantsearch.core.number.decrement
import com.algolia.instantsearch.core.number.increment
import com.algolia.instantsearch.showcase.databinding.ShowcaseFilterComparisonBinding
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.NumericOperator

class FilterPriceView(
    val binding: ShowcaseFilterComparisonBinding,
    price: Attribute,
    operator: NumericOperator
) : NumberView<Long> {

    init {
        binding.stepperText.text = "$price ${operator.raw}"
    }

    override fun setComputation(computation: Computation<Long>) {
        binding.arrowUp.setOnClickListener {
            computation.increment()
        }
        binding.arrowDown.setOnClickListener {
            computation.decrement()
        }
    }

    override fun setText(text: String) {
        binding.stepperNumber.text = text
    }
}