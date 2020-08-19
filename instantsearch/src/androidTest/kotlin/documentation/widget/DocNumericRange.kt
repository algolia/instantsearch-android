package documentation.widget

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.number.range.NumberRangeView
import com.algolia.instantsearch.core.number.range.Range
import com.algolia.instantsearch.core.number.range.connectView
import com.algolia.instantsearch.helper.filter.range.FilterRangeViewModel
import com.algolia.instantsearch.helper.filter.range.connectFilterState
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import org.junit.Ignore


@Ignore
class DocNumericRange {

    private class MySliderNumberRangeView : NumberRangeView<Int> {

        override var onRangeChanged: Callback<Range<Int>>? = null
        override fun setRange(range: Range<Int>?) = Unit
        override fun setBounds(bounds: Range<Int>?) = Unit
    }

    class MyActivity : AppCompatActivity() {

        val client = ClientSearch(
            ApplicationID("YourApplicationID"),
            APIKey("YourAPIKey")
        )
        val index = client.initIndex(IndexName("YourIndexName"))
        val searcher = SearcherSingleIndex(index)
        val filterState = FilterState()
        val attribute = Attribute("price")
        val viewModel = FilterRangeViewModel<Int>()
        val connection = ConnectionHandler()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            val view: NumberRangeView<Int> = MySliderNumberRangeView()

            connection += searcher.connectFilterState(filterState)
            connection += viewModel.connectFilterState(filterState, attribute)
            connection += viewModel.connectView(view)

            searcher.searchAsync()
        }

        override fun onDestroy() {
            super.onDestroy()
            connection.disconnect()
            searcher.cancel()
        }
    }
}