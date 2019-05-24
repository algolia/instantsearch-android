package filter.clear

import com.algolia.instantsearch.helper.filter.clear.ClearFilterViewModel
import com.algolia.instantsearch.helper.filter.clear.connectView
import com.algolia.search.model.search.Facet
import shouldNotEqual
import kotlin.test.Test

class TestClearFilterConnectView {

    private val red = Facet("red", 1)
    private val facets = listOf(red)

    @Test
    fun connectShouldSetOnClick() {
        val viewModel = ClearFilterViewModel()
        val view = MockClearFilterView()
        viewModel.connectView(view)
        view.onClick shouldNotEqual null
    }
}