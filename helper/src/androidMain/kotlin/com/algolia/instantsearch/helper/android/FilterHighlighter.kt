package com.algolia.instantsearch.helper.android

import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import com.algolia.search.model.filter.FilterGroup
import com.algolia.search.model.filter.FilterGroupsConverter


public fun List<FilterGroup<*>>.highlight(
    converter: FilterGroupsConverter<List<FilterGroup<*>>, String?> = FilterGroupsConverter.SQL.Unquoted,
    colors: Map<String, Int> = mapOf(),
    defaultColor: Int = Color.BLACK
): SpannableStringBuilder {
    return SpannableStringBuilder().also {
        var begin = 0

        forEachIndexed { index, group ->
            val color = colors.getOrElse(group.name ?: "") { defaultColor }
            val string = converter(listOf(group))

            it.append(string)
            it.setSpan(ForegroundColorSpan(color), begin, it.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            if (index < lastIndex) {
                begin = it.length
                it.append(" AND ")
                it.setSpan(StyleSpan(Typeface.BOLD), begin, it.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            begin = it.length
        }
    }
}