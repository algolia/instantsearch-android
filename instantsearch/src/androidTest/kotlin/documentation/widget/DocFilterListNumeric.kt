package documentation.widget

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.selectable.list.SelectableItem
import com.algolia.instantsearch.core.selectable.list.connectView
import com.algolia.instantsearch.helper.filter.list.FilterListView
import com.algolia.instantsearch.helper.filter.list.FilterListViewModel
import com.algolia.instantsearch.helper.filter.list.connectFilterState
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator
import org.junit.Ignore

@Ignore
class DocFilterListNumeric {

    private class MyFilterListRecyclerViewAdapter : FilterListView.Numeric {

        override var onSelection: Callback<Filter.Numeric>? = null

        override fun setItems(items: List<SelectableItem<Filter.Numeric>>) = Unit
    }

    class MyActivity : AppCompatActivity() {

        val client = ClientSearch(
            ApplicationID("YourApplicationID"),
            APIKey("YourAPIKey")
        )
        val index = client.initIndex(IndexName("YourIndexName"))
        val searcher = SearcherSingleIndex(index)
        val filterState = FilterState()
        val price = Attribute("price")
        val filters = listOf(
            Filter.Numeric(price, NumericOperator.Less, 5),
            Filter.Numeric(price, 5..10),
            Filter.Numeric(price, 10..25),
            Filter.Numeric(price, 25..100),
            Filter.Numeric(price, NumericOperator.Greater, 100)
        )
        val viewModel = FilterListViewModel.Numeric(filters)
        val connection = ConnectionHandler()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            val view: FilterListView.Numeric = MyFilterListRecyclerViewAdapter()

            connection += searcher.connectFilterState(filterState)
            connection += viewModel.connectFilterState(filterState)
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
