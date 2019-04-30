package selection


public interface SelectableView<T> {

    var onClick: ((T?) -> Unit)?

    fun setSelectableItem(selectableItem: SelectableItem<T>?)
}