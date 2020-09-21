package com.algolia.instantsearch.helper.android.highlighting

import android.graphics.Typeface
import android.text.ParcelableSpan
import android.text.SpannedString
import android.text.style.CharacterStyle
import android.text.style.StyleSpan
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import com.algolia.instantsearch.core.highlighting.HighlightedString

public fun HighlightedString.toSpannedString(
    span: ParcelableSpan = StyleSpan(Typeface.BOLD),
): SpannedString = buildSpannedString {
    tokens.forEach { (part, isHighlighted) ->
        if (isHighlighted) inSpans(span.wrap()) { append(part) }
        else append(part)
    }
}

/**
 * A given [CharacterStyle] can only applied to a single region of a given Spanned.
 * This method wraps a [ParcelableSpan] with a new object (if it is a [CharacterStyle]) that will have the same effect.
 */
internal fun ParcelableSpan.wrap(): Any = if (this is CharacterStyle) CharacterStyle.wrap(this) else this

public fun List<HighlightedString>.toSpannedString(
    span: ParcelableSpan = StyleSpan(Typeface.BOLD),
): SpannedString {
    return buildSpannedString {
        this@toSpannedString.map { it.toSpannedString(span) }.forEachIndexed { index, spanned ->
            if (index > 0) append(", ")
            append(spanned)
        }
    }
}
