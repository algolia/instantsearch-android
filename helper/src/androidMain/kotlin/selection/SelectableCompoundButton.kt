package selection

import android.widget.CompoundButton


class SelectableCompoundButton(
    val view: CompoundButton
) : SelectableView {

    override var onClick: ((Boolean) -> Unit)? = null

    init {
        setOnCheckedChangeListener()
    }

    override fun setText(text: String) {
        view.text = text
    }

    override fun setSelected(isSelected: Boolean) {
        view.setOnCheckedChangeListener(null)
        view.isChecked = isSelected
        setOnCheckedChangeListener()
    }

    private fun setOnCheckedChangeListener() {
        view.setOnCheckedChangeListener { _, isChecked -> onClick?.invoke(isChecked) }
    }
}