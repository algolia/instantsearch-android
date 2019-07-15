package searchbox

import android.widget.EditText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import applicationContext
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.searchbox.connectView
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewEditText
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

    @Test
    fun connectShouldUpdateText() {
        val view = view()
        val viewModel = SearchBoxViewModel()

        viewModel.query.set(text)
        viewModel.connectView(view)
        view.editText.text.toString() shouldEqual text
    }

    @Test
    fun onTextChangedShouldUpdateItem() {
        val view = view()
        val viewModel = SearchBoxViewModel()

        viewModel.connectView(view)
        view.editText.setText(text)
        viewModel.item shouldEqual text
    }
}