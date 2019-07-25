package searchbox

import android.widget.EditText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import applicationContext
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.searchbox.connectionView
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewEditText
import org.junit.Test
import org.junit.runner.RunWith
import shouldEqual


@SmallTest
@RunWith(AndroidJUnit4::class)
class TestSearchBoxEditText {

    private val text = "text"

    private fun view() = SearchBoxViewEditText(EditText(applicationContext))

    @Test
    fun connectShouldUpdateText() {
        val view = view()
        val viewModel = SearchBoxViewModel()
        val connection = viewModel.connectionView(view)

        viewModel.query.value = text
        connection.connect()
        view.editText.text.toString() shouldEqual text
    }

    @Test
    fun onTextChangedShouldUpdateItem() {
        val view = view()
        val viewModel = SearchBoxViewModel()
        val connection = viewModel.connectionView(view)

        connection.connect()
        view.editText.setText(text)
        viewModel.query.value shouldEqual text
    }
}