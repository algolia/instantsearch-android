package selectable

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.selectable.SelectableItemView
import com.algolia.instantsearch.core.selectable.SelectableItemViewModel
import com.algolia.instantsearch.core.selectable.connectView
import shouldEqual
import shouldNotBeNull
import kotlin.test.Test


class TestFilterToggleConnectView {

    private val input = 0
    private val output = "0"
    private val presenter: (Int) -> String = { it.toString() }

    private class MockSelectableItemView : SelectableItemView<String> {

        var boolean: Boolean? = null
        var string: String? = null

        override var onSelectionChanged: Callback<Boolean>? = null

        override fun setIsSelected(isSelected: Boolean) {
            boolean = isSelected
        }

        override fun setItem(item: String) {
            string = item
        }
    }

    @Test
    fun connectShouldCallSetIsSelectedAndSetItem() {
        val view = MockSelectableItemView()
        val viewModel = SelectableItemViewModel(input)

        viewModel.isSelected.value = true
        viewModel.connectView(view, presenter)
        view.boolean shouldEqual true
        view.string shouldEqual output
    }

    @Test
    fun onItemChangedShouldCallSetItem() {
        val view = MockSelectableItemView()
        val viewModel = SelectableItemViewModel(input)

        viewModel.connectView(view, presenter)
        viewModel.item.value = 1
        view.string shouldEqual "1"
    }

    @Test
    fun onClickShouldCallOnIsSelectedComputed() {
        val view = MockSelectableItemView()
        val viewModel = SelectableItemViewModel(input)

        viewModel.eventSelection.subscribe { viewModel.isSelected.value = it }
        viewModel.connectView(view, presenter)
        view.onSelectionChanged.shouldNotBeNull()
        view.onSelectionChanged!!(true)
        view.boolean shouldEqual true
    }

    @Test
    fun onIsSelectedChangedShouldCallSetIsSelected() {
        val view = MockSelectableItemView()
        val viewModel = SelectableItemViewModel(input)

        viewModel.connectView(view, presenter)
        viewModel.isSelected.value = true
        view.boolean shouldEqual true
    }
}