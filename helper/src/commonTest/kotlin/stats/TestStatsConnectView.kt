package stats

import com.algolia.instantsearch.helper.stats.StatsView
import com.algolia.instantsearch.helper.stats.StatsViewModel
import com.algolia.instantsearch.helper.stats.connectView
import com.algolia.search.model.response.ResponseSearch
import shouldBeNull
import shouldBeTrue
import shouldEqual
import kotlin.test.Test


class TestStatsConnectView {

    private val text = "text"
    private val response = ResponseSearch()
    private val presenter : (ResponseSearch?) -> String = { if (it != null) text else ""  }

    private class MockStatsView: StatsView {

        var string : String? = null

        override fun setItem(item: String) {
            string = item
        }
    }

    @Test
    fun connectShouldSetItem() {
        val viewModel = StatsViewModel().also { it.item = response }
        val view = MockStatsView()

        view.string.shouldBeNull()
        viewModel.connectView(view, presenter)
        view.string shouldEqual text
    }

    @Test
    fun onItemChangedShouldSetItem() {
        val viewModel = StatsViewModel()
        val view = MockStatsView()

        viewModel.connectView(view, presenter)
        view.string!!.isEmpty().shouldBeTrue()
        viewModel.item = response
        view.string shouldEqual text
    }
}