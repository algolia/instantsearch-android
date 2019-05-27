package searchbox

import android.widget.EditText
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import applicationContext
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewEditText
import com.algolia.instantsearch.helper.searchbox.connectView
import com.algolia.instantsearch.helper.android.searchbox.connectEditText
import com.algolia.instantsearch.helper.searchbox.connectSearcher
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import mockIndex
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import shouldEqual


@SmallTest
@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class TestSearchBoxEditText {

    private val text = "text"

    private fun view() = SearchBoxViewEditText(EditText(applicationContext))

    private fun editText(): EditText = EditText(applicationContext)

    @Test
    fun connectShouldUpdateQuery() {
        val view = view()
        val viewModel = SearchBoxViewModel()

        viewModel.query = text
        viewModel.connectView(view)
        view.editText.text.toString() shouldEqual text
    }

    @Test
    fun onTextChangedShouldUpdateQuery() {
        val view = view()
        val viewModel = SearchBoxViewModel()

        viewModel.connectView(view)
        view.editText.setText(text)
        viewModel.query shouldEqual text
    }

    @Test
    fun onSearcherQueryChangedShouldUpdateView() {
        val editText = editText()
        val searcher = SearcherSingleIndex(mockIndex)
        val searchBoxViewModel = SearchBoxViewModel()

        searchBoxViewModel.connectEditText(editText)
        searchBoxViewModel.connectSearcher(searcher)

        searcher.setQuery("test")

        editText.text.toString() shouldEqual "test"
    }
}