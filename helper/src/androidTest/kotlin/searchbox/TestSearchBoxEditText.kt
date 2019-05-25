package searchbox

import android.widget.EditText
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewEditText
import com.algolia.instantsearch.helper.searchbox.connectView
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import shouldEqual


@SmallTest
@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class TestSearchBoxEditText {

    private val text = "text"

    private fun view() = SearchBoxViewEditText(EditText(ApplicationProvider.getApplicationContext()))

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
}