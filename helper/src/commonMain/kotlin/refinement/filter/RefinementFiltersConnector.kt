package refinement.filter

import com.algolia.search.model.Attribute
import filter.FilterGroupID
import filter.Filters
import refinement.RefinementOperator
import refinement.RefinementOperator.Or
import refinement.toGroupID
import search.SearcherSingleIndex
import search.addFacet


fun RefinementFiltersViewModel.connectSearcher(
    attribute: Attribute,
    searcher: SearcherSingleIndex,
    operator: RefinementOperator = Or,
    groupName: String = attribute.raw
) {
    fun whenSelectedComputedThenUpdateFilterState(groupID: FilterGroupID) {
        onSelectedComputed += { computed ->
            searcher.filterState.notify {
                items[selected]?.let { remove(groupID, it) }
                items[computed]?.let { add(groupID, it) }
            }
        }
    }

    fun whenFilterStateChangedThenUpdateSelected(groupID: FilterGroupID) {
        val onChange: (Filters) -> Unit = { filters ->
            selected = items.entries.find { it.value == filters.getFilters(groupID).firstOrNull() }?.key
        }

        onChange(searcher.filterState)
        searcher.filterState.onChange += onChange
    }

    val groupID = operator.toGroupID(groupName)

    searcher.query.addFacet(attribute)
    whenSelectedComputedThenUpdateFilterState(groupID)
    whenFilterStateChangedThenUpdateSelected(groupID)
}

fun RefinementFiltersViewModel.connectView(
    view: RefinementFiltersView,
    presenter: RefinementFilterPresenter = RefinementFilterPresenter()
) {
    view.setItems(items.map { it.key to presenter(it.value) }.toMap())
    view.setSelected(selected)
    view.onClick = (::computeSelected)
    onSelectedChanged += (view::setSelected)
}