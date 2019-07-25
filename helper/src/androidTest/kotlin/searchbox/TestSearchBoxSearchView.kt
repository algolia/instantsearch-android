package searchbox

import android.widget.SearchView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import applicationContext
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.searchbox.connectionView
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewImpl
import org.junit.Test
import org.junit.runner.RunWith
import shouldEqual


@SmallTest
@RunWith(AndroidJUnit4::class)
class TestSearchBoxSearchView {

    private val text = "text"

    private fun view() = SearchBoxViewImpl(SearchView(applicationContext))

    @Test
    fun connectShouldUpdateQuery() {
        val view = view()
        val viewModel = SearchBoxViewModel()
        val connection = viewModel.connectionView(view)

        viewModel.query.value = text
        connection.connect()
        view.searchView.query.toString() shouldEqual text
    }

    @Test
    fun setQueryShouldUpdateItem() {
        val view = view()
        val viewModel = SearchBoxViewModel()
        val connection = viewModel.connectionView(view)

        connection.connect()
        view.searchView.setQuery(text, false)
        viewModel.query.value shouldEqual text
    }

    @Test
    fun sendEventShouldCallSubscription() {
        val view = view()
        val viewModel = SearchBoxViewModel()
        var expected: String? = null
        val connection = viewModel.connectionView(view)

        viewModel.eventSubmit.subscribe { expected = it }
        connection.connect()
        view.searchView.setQuery(text, true)
        expected shouldEqual text
    }
}