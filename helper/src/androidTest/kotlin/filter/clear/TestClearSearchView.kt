package filter.clear

import android.view.View
import android.widget.SearchView
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.algolia.instantsearch.helper.filter.clear.ClearFilterView
import com.algolia.instantsearch.helper.filter.clear.ClearFilterViewModel
import com.algolia.instantsearch.helper.filter.clear.connectState
import com.algolia.instantsearch.helper.filter.clear.connectView
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import index
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertEquals


@SmallTest
@RunWith(AndroidJUnit4::class)
@Config(manifest=Config.NONE)
class TestClearSearchView {

    private fun searchView() = SearchView(ApplicationProvider.getApplicationContext())
    private fun clearFilterView() = object : ClearFilterView {
        public val view = View(ApplicationProvider.getApplicationContext())
        override var onClick: (() -> Unit)? = null
            set(value) {
                field = value
                view.setOnClickListener {
                    field?.invoke()
                }
            }
    }

    @Test
    fun clearAllClearsSearchViewQuery() {
        val searchView = searchView()
        val clearFilterView = clearFilterView()
        val searcher = SearcherSingleIndex(index)

        //TODO move this logic to connector once SB PR is merged
        searcher.onQueryChanged += { text : String? ->
            searchView.setQuery(text, false)
        }

        val viewModel = ClearFilterViewModel()
        viewModel.connectState(searcher.filterState, searcher)
        viewModel.connectView(clearFilterView)
        clearFilterView.view.callOnClick()

        assertEquals("", searchView.query.toString())
    }
}