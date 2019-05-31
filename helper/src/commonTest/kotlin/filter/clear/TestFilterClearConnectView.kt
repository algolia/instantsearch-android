package filter.clear

import com.algolia.instantsearch.helper.filter.clear.FilterClearView
import com.algolia.instantsearch.helper.filter.clear.FilterClearViewModel
import com.algolia.instantsearch.helper.filter.clear.connectView
import shouldBeFalse
import shouldBeTrue
import shouldNotBeNull
import kotlin.test.Test


class TestFilterClearConnectView {

    private class MockFilterClearView: FilterClearView {

        override var onClick: ((Unit) -> Unit)? = null
    }

    @Test
    fun connectSetsOnClick() {
        val viewModel = FilterClearViewModel()
        val view = MockFilterClearView()
        var clicked = false

        viewModel.onClicked += { clicked = true }
        clicked.shouldBeFalse()
        viewModel.connectView(view)
        view.onClick.shouldNotBeNull()
        view.onClick?.invoke(Unit)
        clicked.shouldBeTrue()
    }
}
