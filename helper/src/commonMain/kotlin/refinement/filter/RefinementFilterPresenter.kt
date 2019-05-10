package refinement.filter

import com.algolia.search.model.filter.Filter


public object RefinementFilterPresenter: (Filter) -> String {

    override fun invoke(filter: Filter): String {
        return when (filter) {
            is Filter.Facet -> {
                when (val value = filter.value) {
                    is Filter.Facet.Value.String -> value.raw
                    is Filter.Facet.Value.Number -> "${filter.attribute.raw}: ${value.raw}"
                    is Filter.Facet.Value.Boolean -> filter.attribute.raw
                }
            }
            is Filter.Tag -> filter.value
            is Filter.Numeric -> when (val value = filter.value) {
                is Filter.Numeric.Value.Comparison -> "${filter.attribute} ${value.operator.raw} ${value.number}"
                is Filter.Numeric.Value.Range -> "${filter.attribute}: ${value.lowerBound} to ${value.upperBound}"
            }
        }
    }
}