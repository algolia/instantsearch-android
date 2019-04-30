package selection


public interface SelectableListView<T> {

    var onClick: ((T) -> Unit)?

    fun setSelectableItems(selectableItems: List<SelectableItem<T>>)
}