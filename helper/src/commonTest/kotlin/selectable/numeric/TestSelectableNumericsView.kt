package selectable.numeric

import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator
import shouldEqual
import shouldNotBeNull
import kotlin.test.Test


class TestSelectableNumericsView  {

    private val price = Attribute("price")
    private val filter = Filter.Numeric(price, NumericOperator.Greater, 5)
    private val filters = listOf(filter)
    private val selections = setOf(filter)

    private class MockSelectableFacetsView : SelectableNumericsView {

        var items: List<SelectableNumeric> = listOf()

        override var onClick: ((Filter.Numeric) -> Unit)? = null

        override fun setSelectableItems(selectableItems: List<SelectableNumeric>) {
            items = selectableItems
        }
    }

    @Test
    fun connectShouldCallSetSelectableItems() {
        val view = MockSelectableFacetsView()
        val viewModel = SelectableNumericsViewModel(filters, filter)

        viewModel.connectView(view)
        view.items shouldEqual listOf(filter to true)
    }

    @Test
    fun onClickShouldCallOnSelectionsComputed() {
        val view = MockSelectableFacetsView()
        val viewModel = SelectableNumericsViewModel(filters)

        viewModel.onSelectionsComputed += { viewModel.selections = it }
        viewModel.connectView(view)
        view.onClick.shouldNotBeNull()
        view.onClick!!(filter)
        view.items shouldEqual listOf(filter to true)
    }

    @Test
    fun onItemsChangedShouldCallSetSelectableItems() {
        val view = MockSelectableFacetsView()
        val viewModel = SelectableNumericsViewModel()

        viewModel.connectView(view)
        viewModel.items = filters
        view.items shouldEqual listOf(filter to false)
    }

    @Test
    fun onSelectionsChangedShouldCallSetSelectableItems() {
        val view = MockSelectableFacetsView()
        val viewModel = SelectableNumericsViewModel(filters)

        viewModel.connectView(view)
        viewModel.selections = selections
        view.items shouldEqual listOf(filter to true)
    }
}