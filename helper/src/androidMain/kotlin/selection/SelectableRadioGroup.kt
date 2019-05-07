package selection

import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import refinement.filters.RefinementFiltersView


class SelectableRadioGroup(
    val view: RadioGroup
) : RefinementFiltersView {

    override var onClick: ((Int) -> Unit)? = null

    init {
        view.setOnCheckedChangeListener { _, checkedId ->
            onClick?.invoke(checkedId)
        }
    }

    override fun setSelected(selected: Int?) {
        view.check(selected ?: View.NO_ID)
    }

    override fun setItems(items: Map<Int, String>) {
        for (index in 0 until view.childCount) {
            val view = view.getChildAt(index) as? RadioButton

            items[view?.id]?.let { view?.text = it }
        }
    }
}