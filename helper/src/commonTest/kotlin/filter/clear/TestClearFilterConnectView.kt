package filter.clear

import com.algolia.instantsearch.helper.filter.clear.ClearFilterViewModel
import com.algolia.instantsearch.helper.filter.clear.connectView
import shouldNotEqual
import kotlin.test.Test

class TestClearFilterConnectView {
    @Test
    fun connectShouldSetOnClick() {
        val viewModel = ClearFilterViewModel()
        val view = MockClearFilterView()
        viewModel.connectView(view)
        view.onClick shouldNotEqual null
    }
}