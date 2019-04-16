package com.algolia.instantsearch.sample.refinement

import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.FilterGroup
import com.algolia.search.model.filter.FilterGroupsConverter
import refinement.RefinementFacetsPresenter
import refinement.SortCriterion
import refinement.RefinementMode
import search.GroupID


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


fun Map<GroupID, Set<Filter.Facet>>.highlight(
    converter: FilterGroupsConverter<List<FilterGroup<*>>, String?>,
    colors: Map<String, Int> = mapOf()
): SpannableStringBuilder {
    return SpannableStringBuilder().also {
        var begin = 0

        entries.forEachIndexed { index, (key, value) ->
            val color = colors.getOrElse(key.name) { Color.BLACK }
            val group =  when (key) {
                is GroupID.And -> FilterGroup.And.Facet(value)
                is GroupID.Or -> FilterGroup.Or.Facet(value)
            }
            val string = converter(listOf(group))

            it.append(string)
            it.setSpan(ForegroundColorSpan(color), begin, it.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            if (index < entries.size - 1) {
                begin = it.length
                it.append(" AND ")
                it.setSpan(StyleSpan(Typeface.BOLD), begin, it.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            begin = it.length
        }
    }
}