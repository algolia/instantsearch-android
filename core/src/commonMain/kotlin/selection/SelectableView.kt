package selection


public interface SelectableView<T> {

    var onClick: ((Boolean) -> Unit)?

    fun setSelectableItem(selectableItem: SelectableItem<T>)
}