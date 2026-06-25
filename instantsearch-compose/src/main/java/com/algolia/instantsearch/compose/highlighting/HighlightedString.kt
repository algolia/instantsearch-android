package com.algolia.instantsearch.compose.highlighting

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.algolia.instantsearch.core.highlighting.HighlightedString

/**
 * Converts a [HighlightedString] to [AnnotatedString].
 *
 * @param spanStyle applied highlighting style
 */
// `tokens` is a Kotlin `List`, so `forEach` resolves to the inlined
// `kotlin.collections.forEach` extension — not `java.lang.Iterable#forEach`
// (API 24). The NewApi flag here is a lint false positive.
@Suppress("NewApi")
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

/**
 * Converts a list of [HighlightedString] to a list of [AnnotatedString].
 *
 * @param spanStyle applied highlighting style
 */
public fun List<HighlightedString>.toAnnotatedString(
    spanStyle: SpanStyle = SpanStyle(fontWeight = FontWeight.Bold)
): AnnotatedString {
    return with(AnnotatedString.Builder()) {
        map { it.toAnnotatedString(spanStyle) }.forEachIndexed { index, annotated ->
            if (index > 0) {
                append(", ")
            }
            append(annotated)
        }

        toAnnotatedString()
    }
}
