package selectable.map

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.selectable.map.SelectableMapView
import com.algolia.instantsearch.core.selectable.map.SelectableMapViewModel
import com.algolia.instantsearch.core.selectable.map.connectView
import shouldEqual
import shouldNotBeNull
import kotlin.test.Test

class TestSelectableMapConnectView {

    private val input = 0
    private val output = "0"
    private val id = 0
    private val map = mapOf(id to input)
    private val presenter: (Int) -> String = { it.toString() }

    private class MockSelectableView : SelectableMapView<Int, String> {

        override var onSelectionChange: Callback<Int>? = null

        var int: Int? = null
        var data: Map<Int, String> = mapOf()

        override fun setMap(map: Map<Int, String>) {
            data = map
        }

        override fun setSelected(selected: Int?) {
            int = selected
        }
    }

    @Test
    fun connectShouldCallSetSelectedAndSetItem() {
        val view = MockSelectableView()
        val viewModel = SelectableMapViewModel(map)
        val connection = viewModel.connectView(view, presenter)

        viewModel.selected.value = id
        connection.connect()
        view.int shouldEqual id
        view.data shouldEqual mapOf(id to output)
    }

    @Test
    fun onItemChangedShouldCallSetItem() {
        val view = MockSelectableView()
        val viewModel = SelectableMapViewModel(map)
        val connection = viewModel.connectView(view, presenter)

        connection.connect()
        viewModel.map.value = mapOf(1 to 1)
        view.data shouldEqual mapOf(1 to "1")
    }

    @Test
    fun onClickShouldCallOnSelectionsComputed() {
        val view = MockSelectableView()
        val viewModel = SelectableMapViewModel(map)
        val connection = viewModel.connectView(view, presenter)

        viewModel.eventSelection.subscribe { viewModel.selected.value = it }
        connection.connect()
        view.onSelectionChange.shouldNotBeNull()
        view.onSelectionChange!!(id)
        view.int shouldEqual id
    }

    @Test
    fun onSelectedChangedShouldCallSetSelected() {
        val view = MockSelectableView()
        val viewModel = SelectableMapViewModel(map)
        val connection = viewModel.connectView(view, presenter)

        connection.connect()
        viewModel.selected.value = id
        view.int shouldEqual id
    }
}
