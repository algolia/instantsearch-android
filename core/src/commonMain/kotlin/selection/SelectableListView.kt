package selection


public interface SelectableListView<T> {

    fun setSelectableItems(selectableItems: List<SelectableItem<T>>)

    fun onClickItem(onClick: (T) -> Unit)
}