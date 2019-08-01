package com.algolia.instantsearch.demo.filter.numeric.comparison

import android.view.View
import com.algolia.instantsearch.core.number.Computation
import com.algolia.instantsearch.core.number.NumberView
import com.algolia.instantsearch.core.number.decrement
import com.algolia.instantsearch.core.number.increment
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.NumericOperator
import kotlinx.android.synthetic.main.demo_filter_comparison.view.*


class FilterPriceView(
    val view: View,
    val price: Attribute,
    val operator: NumericOperator
): NumberView<Long> {

    init {
        view.stepperText.text = "$price ${operator.raw}"
    }

    override fun setComputation(computation: Computation<Long>) {
        view.arrowUp.setOnClickListener {
            computation.increment()
        }
        view.arrowDown.setOnClickListener {
            computation.decrement()
        }
    }

    override fun setText(text: String) {
        view.stepperNumber.text = text
    }
}