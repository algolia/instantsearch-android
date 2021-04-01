package com.algolia.instantsearch.compose.highlight

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.algolia.instantsearch.core.highlighting.HighlightedString

public fun HighlightedString.toAnnotatedString(spanStyle: SpanStyle = SpanStyle(fontWeight = FontWeight.Bold)): AnnotatedString {
    return with(AnnotatedString.Builder()) {
        tokens.forEach { (part, isHighlighted) ->
            if (isHighlighted) {
                withStyle(spanStyle) {
                    append(part)
                }
            } else {
                append(part)
            }
        }
        toAnnotatedString()
    }
}

public fun List<HighlightedString>.toAnnotatedString(
    spanStyle: SpanStyle = SpanStyle(fontWeight = FontWeight.Bold)
): AnnotatedString {
    return with(AnnotatedString.Builder()) {
        map { it.toAnnotatedString(spanStyle) }.forEachIndexed { index, annotated ->
            if (index > 0) append(", ")
            append(annotated)
        }

        toAnnotatedString()
    }
}
