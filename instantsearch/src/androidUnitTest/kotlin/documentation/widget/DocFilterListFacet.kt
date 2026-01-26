package documentation.widget

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.selectable.list.SelectableItem
import com.algolia.instantsearch.core.selectable.list.connectView
import com.algolia.instantsearch.filter.list.FilterListView
import com.algolia.instantsearch.filter.list.FilterListViewModel
import com.algolia.instantsearch.filter.list.connectFilterState
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.searcher.connectFilterState
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.search.client.ClientSearch
import com.algolia.instantsearch.filter.Filter
import org.junit.Ignore

@Ignore
internal class DocFilterListFacet {

    private class MyFilterListRecyclerViewAdapter : FilterListView.Facet {

        override var onSelection: Callback<Filter.Facet>? = null

        override fun setItems(items: List<SelectableItem<Filter.Facet>>) = Unit
    }

    class MyActivity : AppCompatActivity() {

        val client = ClientSearch(
            "YourApplicationID",
            "YourAPIKey"
        )
        val searcher = HitsSearcher(client, "YourIndexName")
        val filterState = FilterState()
        val color = "color"
        val filters = listOf(
            Filter.Facet(color, "red"),
            Filter.Facet(color, "green"),
            Filter.Facet(color, "blue"),
            Filter.Facet(color, "yellow"),
            Filter.Facet(color, "black")
        )
        val viewModel = FilterListViewModel.Facet(filters)
        val connection = ConnectionHandler()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            val view: FilterListView.Facet = MyFilterListRecyclerViewAdapter()

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
