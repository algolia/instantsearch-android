package filter.clear

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import clearFilterView
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.helper.android.searchbox.connectSearchView
import com.algolia.instantsearch.helper.filter.clear.ClearFilterViewModel
import com.algolia.instantsearch.helper.filter.clear.connectState
import com.algolia.instantsearch.helper.filter.clear.connectView
import com.algolia.instantsearch.helper.searchbox.connectSearcher
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import mockIndex
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import searchView
import shouldEqual


@SmallTest
@RunWith(AndroidJUnit4::class)
@Config(manifest=Config.NONE)
class TestClearFilter {
    @Test
    fun clearAllClearsSearchViewQuery() {
        val searchView = searchView()
        val clearFilterView = clearFilterView()
        val searcher = SearcherSingleIndex(mockIndex)

        val searchBoxViewModel = SearchBoxViewModel()
        searchBoxViewModel.connectSearchView(searchView)
        searchBoxViewModel.connectSearcher(searcher)

        val viewModel = ClearFilterViewModel()
        viewModel.connectState(searcher.filterState, searcher)
        viewModel.connectView(clearFilterView)
        clearFilterView.view.callOnClick()

        searchView.query.toString() shouldEqual ""
    }
}