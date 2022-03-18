package instrumented.searchbox

import android.os.Build
import android.widget.AutoCompleteTextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.searchbox.connectView
import com.algolia.instantsearch.android.searchbox.SearchBoxAutoCompleteTextView
import instrumented.applicationContext
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import shouldEqual

@SmallTest
@Config(sdk = [Build.VERSION_CODES.P])
@RunWith(AndroidJUnit4::class)
class TestSearchBoxAutoCompleteTextView {

    private val text = "text"

    private fun view() = SearchBoxAutoCompleteTextView(AutoCompleteTextView(applicationContext))

    @Test
    fun connectShouldUpdateText() {
        val view = view()
        val viewModel = SearchBoxViewModel()
        val connection = viewModel.connectView(view)

        viewModel.query.value = text
        connection.connect()
        view.autoCompleteTextView.text.toString() shouldEqual text
    }

    @Test
    fun onTextChangedShouldUpdateItem() {
        val view = view()
        val viewModel = SearchBoxViewModel()
        val connection = viewModel.connectView(view)

        connection.connect()
        view.autoCompleteTextView.setText(text)
        viewModel.query.value shouldEqual text
    }
}
