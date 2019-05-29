package filter.clear

import com.algolia.instantsearch.helper.filter.clear.ClearFiltersViewModel
import com.algolia.instantsearch.helper.filter.clear.connectView
import shouldNotEqual
import kotlin.test.Test

class TestClearFiltersConnectView {
    @Test
    fun connectShouldSetOnClick() {
        val viewModel = ClearFiltersViewModel()
        val view = MockClearFiltersView()
        viewModel.connectView(view)
        view.onClick shouldNotEqual null
    }
}