package selectable

import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import selectable.filters.SelectableFiltersView


class SelectableRadioGroup(
    val view: RadioGroup
) : SelectableFiltersView {

    override var onClick: ((Int) -> Unit)? = null

    init {
        setOnCheckedChangeListener()
    }

    override fun setSelected(selected: Int?) {
        view.setOnCheckedChangeListener(null)
        view.check(selected ?: View.NO_ID)
        setOnCheckedChangeListener()
    }

    override fun setItems(items: Map<Int, String>) {
        for (index in 0 until view.childCount) {
            val view = view.getChildAt(index) as? RadioButton

            items[view?.id]?.let { view?.text = it }
        }
    }
    private fun setOnCheckedChangeListener() {
        view.setOnCheckedChangeListener { _, isChecked -> onClick?.invoke(isChecked) }
    }
}