package selection


public interface SelectableView {

    var onClick: ((Boolean) -> Unit)?

    fun setSelected(isSelected: Boolean)

    fun setText(text: String)
}