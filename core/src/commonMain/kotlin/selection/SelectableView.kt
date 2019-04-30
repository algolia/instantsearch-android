package selection


public interface SelectableView<T> {

    var onClick: (() -> Unit)?

    fun setSelectableItem(selectableItem: SelectableItem<T>)
}