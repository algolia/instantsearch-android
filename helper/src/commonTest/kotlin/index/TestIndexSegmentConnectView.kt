package index

import com.algolia.instantsearch.core.selectable.segment.SelectableSegmentView
import com.algolia.instantsearch.helper.index.IndexSegmentViewModel
import com.algolia.instantsearch.helper.index.connectView
import com.algolia.search.client.Index
import com.algolia.search.model.IndexName
import mockClient
import shouldEqual
import shouldNotBeNull
import kotlin.test.Test


class TestIndexSegmentConnectView {

    private val client = mockClient()
    private val indexA = client.initIndex(IndexName("A"))
    private val id = 0
    private val indexes = mapOf(id to indexA)

    private val presenter = { index: Index -> index.indexName.raw }

    private class MockSelectableView : SelectableSegmentView<Int, String> {

        var int: Int? = null
        var map: Map<Int, String> = mapOf()

        override var onClick: ((Int) -> Unit)? = null

        override fun setSelected(selected: Int?) {
            int = selected
        }

        override fun setItem(item: Map<Int, String>) {
            map = item
        }
    }

    @Test
    fun connectShouldCallSetSelectedAndSetItems() {
        val view = MockSelectableView()
        val viewModel = IndexSegmentViewModel(indexes)

        viewModel.selected = id
        viewModel.connectView(view, presenter)
        view.int shouldEqual id
        view.map shouldEqual mapOf(id to presenter(indexA))
    }

    @Test
    fun onClickShouldCallOnSelectionsComputed() {
        val view = MockSelectableView()
        val viewModel = IndexSegmentViewModel(indexes)

        viewModel.onSelectedComputed += { viewModel.selected = it }
        viewModel.connectView(view, presenter)
        view.onClick.shouldNotBeNull()
        view.onClick!!(id)
        view.int shouldEqual id
    }

    @Test
    fun onSelectedChangedShouldCallSetSelected() {
        val view = MockSelectableView()
        val viewModel = IndexSegmentViewModel(indexes)

        viewModel.connectView(view, presenter)
        viewModel.selected = id
        view.int shouldEqual id
    }
}