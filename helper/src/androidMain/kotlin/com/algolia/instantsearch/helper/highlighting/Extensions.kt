package com.algolia.instantsearch.helper.highlighting

import android.graphics.Typeface
import android.text.ParcelableSpan
import android.text.SpannedString
import android.text.style.StyleSpan
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import com.algolia.instantsearch.core.highlighting.HighlightedString


public fun HighlightedString.toSpannedString(
    span: ParcelableSpan = StyleSpan(Typeface.BOLD)
): SpannedString = buildSpannedString {
    tokens.forEach { (part, isHighlighted) ->
        if (isHighlighted) inSpans(span) { append(part) }
        else append(part)
    }
}

public fun List<HighlightedString>.toSpannedString(
    span: ParcelableSpan = StyleSpan(Typeface.BOLD)
): SpannedString {
    return buildSpannedString {
        this@toSpannedString.map { it.toSpannedString(span) }.forEachIndexed { index, spanned ->
            if (index > 0) append(", ")
            append(spanned)
        }
    }
}