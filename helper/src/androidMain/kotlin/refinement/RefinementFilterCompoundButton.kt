package refinement

import android.widget.CompoundButton
import selection.SelectableView


class RefinementFilterCompoundButton(
    val view: CompoundButton
) : SelectableView {

    override var onClick: ((Boolean) -> Unit)? = null

    init {
        view.setOnClickListener { onClick?.invoke(view.isChecked) }
    }

    override fun setText(text: String) {
        view.text = text
    }

    override fun setSelected(isSelected: Boolean) {
        view.isChecked = isSelected
    }
}