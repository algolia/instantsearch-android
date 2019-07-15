package searchbox

import android.widget.SearchView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import applicationContext
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.searchbox.connectView
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewImpl
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import shouldEqual


@SmallTest
@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class TestSearchBoxSearchView {

    private val text = "text"

    private fun view() = SearchBoxViewImpl(SearchView(applicationContext))

    @Test
    fun connectShouldUpdateQuery() {
        val view = view()
        val viewModel = SearchBoxViewModel()

        viewModel.query.set(text)
        viewModel.connectView(view)
        view.searchView.query.toString() shouldEqual text
    }

    @Test
    fun setQueryShouldUpdateItem() {
        val view = view()
        val viewModel = SearchBoxViewModel()

        viewModel.connectView(view)
        view.searchView.setQuery(text, false)
        viewModel.query.get() shouldEqual text
    }

    @Test
    fun sendEventShouldCallSubscription() {
        val view = view()
        val viewModel = SearchBoxViewModel()
        var expected: String? = null

        viewModel.event.subscribe { expected = it }
        viewModel.connectView(view)
        view.searchView.setQuery(text, true)
        expected shouldEqual text
    }
}