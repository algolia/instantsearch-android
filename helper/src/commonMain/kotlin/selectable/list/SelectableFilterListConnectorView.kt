package selectable.list

import com.algolia.search.model.filter.Filter


fun <T: Filter> SelectableFilterListViewModel<T>.connectView(
    view: SelectableFilterListView<T>
) {

    fun setSelectableItems(filters: List<T>, selections: Set<T>) {
        val selectableItems = filters.map { it to selections.contains(it) }

        view.setSelectableItems(selectableItems)
    }

    setSelectableItems(items, selections)
    view.onClick = { filter -> computeSelections(filter) }
    onItemsChanged += { items -> setSelectableItems(items, selections) }
    onSelectionsChanged += { selections -> setSelectableItems(items, selections) }
}