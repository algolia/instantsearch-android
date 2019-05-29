package filter.clear

import com.algolia.instantsearch.helper.filter.clear.ClearFiltersView
import com.algolia.instantsearch.helper.filter.clear.ClearFiltersViewModel
import com.algolia.instantsearch.helper.filter.clear.connectView
import shouldNotBeNull
import kotlin.test.Test


class TestClearFiltersConnectView {

    private class MockClearFiltersView : ClearFiltersView {

        override var onClick: (() -> Unit)? = null
    }

    @Test
    fun connectShouldSetOnClick() {
        val viewModel = ClearFiltersViewModel()
        val view = MockClearFiltersView()
        viewModel.connectView(view)
        view.onClick.shouldNotBeNull()
    }
}
