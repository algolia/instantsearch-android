package com.algolia.instantsearch.helper.highlighting

import android.text.SpannedString
import android.text.TextUtils
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import com.algolia.instantsearch.core.highlighting.HighlightedString

public fun HighlightedString.toSpannedString(): SpannedString = buildSpannedString {
    parts.forEach { (part, isHighlighted) ->
        if (isHighlighted) bold { append(part) }
        else append(part)
    }
}

public fun List<HighlightedString>.toSpannedString(): SpannedString {
    return buildSpannedString {
        this@toSpannedString.map { it.toSpannedString() }.let {
            if (it.isNotEmpty()) append(it.reduce { acc, str ->
                TextUtils.concat(acc, ", ", str) as SpannedString
            })
        }
    }
}