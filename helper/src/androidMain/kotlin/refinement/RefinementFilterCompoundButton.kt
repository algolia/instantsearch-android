package refinement

import android.widget.CompoundButton
import com.algolia.search.model.filter.Filter


class RefinementFilterCompoundButton(
    val view: CompoundButton
) : RefinementFilterView {

    override var onClick: ((Boolean) -> Unit)? = null

    init {
        view.setOnClickListener { onClick?.invoke(view.isChecked) }
    }

    override fun setSelectableItem(selectableItem: RefinementFilter) {
        val (filter, isSelected) = selectableItem

        view.text = when (filter) {
            is Filter.Facet -> {
                when (val value = filter.value) {
                    is Filter.Facet.Value.String -> value.raw
                    is Filter.Facet.Value.Number -> value.raw.toString()
                    is Filter.Facet.Value.Boolean -> value.raw.toString()
                }
            }
            is Filter.Tag -> filter.value
            is Filter.Numeric -> when (val value = filter.value) {
                is Filter.Numeric.Value.Comparison -> "${filter.attribute} ${value.operator} ${value.number}"
                is Filter.Numeric.Value.Range -> "${filter.attribute} ${value.lowerBound} to ${value.upperBound}"
            }
        }
        view.isChecked = isSelected
    }
}