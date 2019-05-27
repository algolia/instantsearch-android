
import android.content.Context
import android.view.View
import android.widget.SearchView
import androidx.test.core.app.ApplicationProvider
import com.algolia.instantsearch.helper.filter.clear.ClearFilterView
import com.algolia.search.model.IndexName
import com.algolia.search.model.response.ResponseSearch

private val mockResponse = ResponseSearch()
private val mockClient = mockClient(mockResponse, ResponseSearch.serializer())
public val mockIndex = mockClient.initIndex(IndexName("stub"))

private val applicationContext = ApplicationProvider.getApplicationContext<Context>()
internal fun searchView(): SearchView {
    return SearchView(applicationContext)
}
internal fun clearFilterView() = TestClearFilterView()

internal class TestClearFilterView : ClearFilterView {
    public val view = View(applicationContext)
    override var onClick: (() -> Unit)? = null
        set(value) {
            field = value
            view.setOnClickListener {
                field?.invoke()
            }
        }
}