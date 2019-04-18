package selection


public interface SelectionListView<T> {

    fun setSelectableItems(selectableItems: List<SelectableItem<T>>)

    fun onClickItem(onClick: (T) -> Unit)
}