package filter.segment

import com.algolia.instantsearch.core.selectable.segment.SelectableSegmentView
import com.algolia.instantsearch.helper.filter.FilterPresenterImpl
import com.algolia.instantsearch.helper.filter.segment.FilterSegmentViewModel
import com.algolia.instantsearch.helper.filter.segment.connectView
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import shouldEqual
import shouldNotBeNull
import kotlin.test.Test


class TestFilterSegmentConnectView {

    private val color = Attribute("color")
    private val red = Filter.Facet(color, "red")
    private val id = 0
    private val filters = mapOf(id to red)

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
        val viewModel = FilterSegmentViewModel(filters)

        viewModel.selected = id
        viewModel.connectView(view)
        view.int shouldEqual id
        view.map shouldEqual mapOf(id to FilterPresenterImpl()(red))
    }

    @Test
    fun onClickShouldCallOnSelectionsComputed() {
        val view = MockSelectableView()
        val viewModel = FilterSegmentViewModel(filters)

        viewModel.onSelectedComputed += { viewModel.selected = it }
        viewModel.connectView(view)
        view.onClick.shouldNotBeNull()
        view.onClick!!(id)
        view.int shouldEqual id
    }

    @Test
    fun onSelectedChangedShouldCallSetSelected() {
        val view = MockSelectableView()
        val viewModel = FilterSegmentViewModel(filters)

        viewModel.connectView(view)
        viewModel.selected = id
        view.int shouldEqual id
    }
}