package com.algolia.instantsearch.showcase.compose.filter.range

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.algolia.instantsearch.core.number.range.Range

fun rangeText(range: Range<Int>?): AnnotatedString {
    return with(AnnotatedString.Builder()) {
        append("Range: ")
        if (range != null) {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append("${range.min}")
            }
            append(" to ")
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append("${range.max}")
            }
        } else {
            withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                append("?")
            }
        }
        toAnnotatedString()
    }
}

fun boundsText(bounds: Range<Int>?): String {
    return bounds?.let {
        "Bounds: ${it.min} to ${it.max}"
    } ?: "No bounds"
}

fun Range<Int>.toClosedFloatRange(): ClosedFloatingPointRange<Float> {
    return min.toFloat()..max.toFloat()
}

fun ClosedFloatingPointRange<Float>.toRange(): Range<Int> {
    return Range(start.toInt(), endInclusive.toInt())
}