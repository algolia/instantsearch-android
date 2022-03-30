package com.algolia.instantsearch.showcase.compose.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.algolia.instantsearch.compose.number.range.NumberRangeState
import com.algolia.instantsearch.core.number.range.Range
import com.algolia.instantsearch.showcase.compose.R
import android.widget.RatingBar as RatingBarView

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    ratingState: NumberRangeState<Float>,
    step: Float? = null
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            RatingBarView(context, null, R.attr.ratingBarStyleIndicator).apply {
                setIsIndicator(false)
                step?.let { stepSize = it }
                setOnRatingBarChangeListener { ratingBar, rating, _ ->
                    ratingState.onRangeChanged?.invoke(
                        Range(rating..ratingBar.numStars.toFloat())
                    )
                }
            }
        },
        update = { ratingBar ->
            ratingState.bounds?.let { ratingBar.numStars = it.max.toInt() }
            ratingState.range?.let { ratingBar.rating = it.min }
        }
    )
}
