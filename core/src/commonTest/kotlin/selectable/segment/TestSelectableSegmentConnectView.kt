package selectable.segment

import com.algolia.instantsearch.core.event.Event
import com.algolia.instantsearch.core.selectable.segment.SelectableSegmentView
import com.algolia.instantsearch.core.selectable.segment.SelectableSegmentViewModel
import com.algolia.instantsearch.core.selectable.segment.connectView
import shouldEqual
import shouldNotBeNull
import kotlin.test.Test


class TestSelectableSegmentConnectView {

    private val input = 0
    private val output = "0"
    private val id = 0
    private val map = mapOf(id to input)
    private val presenter: (Int) -> String = { it.toString() }

    private class MockSelectableView : SelectableSegmentView<Int, String> {

        override var onSelectionChange: Event<Int> = null

        var int: Int? = null
        var map: Map<Int, String> = mapOf()

        override fun setSegment(segment: Map<Int, String>) {
            map = segment
        }

        override fun setSelected(selected: Int?) {
            int = selected
        }
    }

    @Test
    fun connectShouldCallSetSelectedAndSetItem() {
        val view = MockSelectableView()
        val viewModel = SelectableSegmentViewModel(map)

        viewModel.selected.value = id
        viewModel.connectView(view, presenter)
        view.int shouldEqual id
        view.map shouldEqual mapOf(id to output)
    }

    @Test
    fun onItemChangedShouldCallSetItem() {
        val view = MockSelectableView()
        val viewModel = SelectableSegmentViewModel(map)

        viewModel.connectView(view, presenter)
        viewModel.segment.value = mapOf(1 to 1)
        view.map shouldEqual mapOf(1 to "1")
    }

    @Test
    fun onClickShouldCallOnSelectionsComputed() {
        val view = MockSelectableView()
        val viewModel = SelectableSegmentViewModel(map)

        viewModel.eventSelection.subscribe { viewModel.selected.value = it }
        viewModel.connectView(view, presenter)
        view.onSelectionChange.shouldNotBeNull()
        view.onSelectionChange!!(id)
        view.int shouldEqual id
    }

    @Test
    fun onSelectedChangedShouldCallSetSelected() {
        val view = MockSelectableView()
        val viewModel = SelectableSegmentViewModel(map)

        viewModel.connectView(view, presenter)
        viewModel.selected.value = id
        view.int shouldEqual id
    }
}