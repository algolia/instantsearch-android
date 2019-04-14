package com.algolia.instantsearch.sample.refinement

import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import com.algolia.search.model.filter.FilterGroup
import com.algolia.search.model.filter.FilterGroupConverter
import refinement.RefinementFacetsPresenter
import refinement.SortCriterion
import refinement.RefinementMode


fun SortCriterion.format(): String {
    return when (this) {
        SortCriterion.IsRefined -> name
        SortCriterion.CountAsc -> name
        SortCriterion.CountDesc -> name
        SortCriterion.AlphabeticalAsc -> "AlphaAsc"
        SortCriterion.AlphabeticalDesc -> "Alphadesc"
    }
}

fun formatTitle(presenter: RefinementFacetsPresenter, refinementMode: RefinementMode): String {
    val criteria = presenter.sortCriteria.joinToString("-") { it.format() }

    return "$refinementMode, $criteria, l=${presenter.limit}"
}


fun List<FilterGroup<*>>.highlight(
    converter: FilterGroupConverter<List<FilterGroup<*>>, String>,
    colors: List<Int> = listOf()
): SpannableStringBuilder {
    return SpannableStringBuilder().also {
        var begin = 0

        forEachIndexed { index, group ->
            val color = colors.getOrElse(index) { Color.BLACK }
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