package instrumented.searchbox

import android.os.Build
import android.widget.SearchView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.searchbox.connectView
import com.algolia.instantsearch.android.searchbox.SearchBoxViewImpl
import instrumented.applicationContext
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import shouldEqual

@SmallTest
@Config(sdk = [Build.VERSION_CODES.P])
@RunWith(AndroidJUnit4::class)
class TestSearchBoxSearchView {

    private val text = "text"

    private fun view() = SearchBoxViewImpl(SearchView(applicationContext))

    @Test
    fun connectShouldUpdateQuery() {
        val view = view()
        val viewModel = SearchBoxViewModel()
        val connection = viewModel.connectView(view)

        viewModel.query.value = text
        connection.connect()
        view.searchView.query.toString() shouldEqual text
    }

    @Test
    fun setQueryShouldUpdateItem() {
        val view = view()
        val viewModel = SearchBoxViewModel()
        val connection = viewModel.connectView(view)

        connection.connect()
        view.searchView.setQuery(text, false)
        viewModel.query.value shouldEqual text
    }

    @Test
    fun sendEventShouldCallSubscription() {
        val view = view()
        val viewModel = SearchBoxViewModel()
        var expected: String? = null
        val connection = viewModel.connectView(view)

        viewModel.eventSubmit.subscribe { expected = it }
        connection.connect()
        view.searchView.setQuery(text, true)
        expected shouldEqual text
    }
}
