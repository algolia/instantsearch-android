package filter.clear

import android.view.View
import android.widget.SearchView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import applicationContext
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.helper.android.searchbox.connectSearchView
import com.algolia.instantsearch.helper.filter.clear.ClearFilterView
import com.algolia.instantsearch.helper.filter.clear.ClearFilterViewModel
import com.algolia.instantsearch.helper.filter.clear.connectFilterState
import com.algolia.instantsearch.helper.filter.clear.connectView
import com.algolia.instantsearch.helper.searchbox.connectSearcher
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import mockIndex
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import shouldEqual


@SmallTest
@RunWith(AndroidJUnit4::class)
@Config(manifest=Config.NONE)
class TestClearFilter {
    private fun clearFilterView() = MockClearFilterView()

    @Test
    fun clearAllClearsSearchViewQuery() {
        val searchView = SearchView(applicationContext)
        val clearFilterView = clearFilterView()
        val searcher = SearcherSingleIndex(mockIndex)

        val searchBoxViewModel = SearchBoxViewModel()
        searchBoxViewModel.connectSearchView(searchView)
        searchBoxViewModel.connectSearcher(searcher)

        val viewModel = ClearFilterViewModel()
        viewModel.connectFilterState(searcher.filterState, searcher)
        viewModel.connectView(clearFilterView)
        clearFilterView.view.callOnClick()

        searchView.query.toString() shouldEqual ""
    }

    internal class MockClearFilterView : ClearFilterView {
        public val view = View(applicationContext)
        override var onClick: (() -> Unit)? = null
            set(value) {
                field = value
                view.setOnClickListener {
                    field?.invoke()
                }
            }
    }
}