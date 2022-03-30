package com.algolia.instantsearch.showcase.filter.rating

import android.widget.RatingBar
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.number.range.NumberRangeView
import com.algolia.instantsearch.core.number.range.Range

class RatingBarView(
    private val ratingBar: RatingBar
) : NumberRangeView<Float> {

    init {
        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, _ ->
            onRangeChanged?.invoke(Range(rating..ratingBar.numStars.toFloat()))
        }
    }

    override var onRangeChanged: Callback<Range<Float>>? = null

    override fun setBounds(bounds: Range<Float>?) {
        bounds?.let { ratingBar.numStars = it.max.toInt() }
    }

    override fun setRange(range: Range<Float>?) {
        range?.let { ratingBar.rating = it.min }
    }

    var rating
        get() = ratingBar.rating
        set(value) {
            ratingBar.rating = value
        }

    var stepSize
        get() = ratingBar.stepSize
        set(value) {
            ratingBar.stepSize = value
        }
}
