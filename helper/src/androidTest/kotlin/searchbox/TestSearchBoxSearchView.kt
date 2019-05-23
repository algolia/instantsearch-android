package searchbox

import android.widget.SearchView
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.helper.android.searchbox.connectSearchView
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import shouldEqual


@SmallTest
@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class TestSearchBoxSearchView {

    private val text = "text"

    private fun searchView() = SearchView(ApplicationProvider.getApplicationContext())

    @Test
    fun connectShouldUpdateQuery() {
        val searchView = searchView()
        val viewModel = SearchBoxViewModel()

        viewModel.query = text
        viewModel.connectSearchView(searchView)
        searchView.query.toString() shouldEqual text
    }

    @Test
    fun onQueryChangedShouldUpdateQuery() {
        val searchView = searchView()
        val viewModel = SearchBoxViewModel()

        viewModel.connectSearchView(searchView)
        searchView.setQuery(text, false)
        viewModel.query shouldEqual text
    }

    @Test
    fun onSubmitShouldCallOnQuerySubmitted() {
        val searchView = searchView()
        val viewModel = SearchBoxViewModel()
        var expected: String? = null

        viewModel.onQuerySubmitted += { expected = it }
        viewModel.connectSearchView(searchView)
        searchView.setQuery(text, true)
        expected shouldEqual text
    }
}