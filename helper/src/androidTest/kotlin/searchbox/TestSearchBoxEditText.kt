package searchbox

import android.widget.EditText
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.helper.android.searchbox.connectEditText
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import shouldEqual


@SmallTest
@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class TestSearchBoxEditText {

    private val text = "text"

    private fun editText() = EditText(ApplicationProvider.getApplicationContext())

    @Test
    fun connectShouldUpdateQuery() {
        val editText = editText()
        val viewModel = SearchBoxViewModel()

        viewModel.query = text
        viewModel.connectEditText(editText)
        editText.text.toString() shouldEqual text
    }

    @Test
    fun onTextChangedShouldUpdateQuery() {
        val editText = editText()
        val viewModel = SearchBoxViewModel()

        viewModel.connectEditText(editText)
        editText.setText(text)
        viewModel.query shouldEqual text
    }
}