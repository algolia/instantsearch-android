package documentation.widget

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.selectable.list.SelectableItem
import com.algolia.instantsearch.helper.filter.facet.*
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.search.Facet
import org.junit.Ignore


@Ignore
class DocFacetList {

    private class MyFacetListRecyclerViewAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>(), FacetListView {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = throw Exception()
        override fun getItemCount(): Int = 0
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = Unit
        override var onSelection: Callback<Facet>? = null
        override fun setItems(items: List<SelectableItem<Facet>>) = Unit
    }

    class MyActivity: AppCompatActivity() {

        val client = ClientSearch(
            ApplicationID("YourApplicationID"),
            APIKey("YourAPIKey")
        )
        val index = client.initIndex(IndexName("YourIndexName"))
        val searcher = SearcherSingleIndex(index)
        val filterState = FilterState()
        val attribute = Attribute("facetName")
        val viewModel = FacetListViewModel()
        val presenter = FacetListPresenterImpl(listOf(FacetSortCriterion.CountDescending), limit = 5)
        val connection = ConnectionHandler()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            // Implement the FacetListView interface on the UI component of your choice.
            // For example, a RecyclerView.Adapter
            val view: FacetListView = MyFacetListRecyclerViewAdapter()

            connection += searcher.connectFilterState(filterState)
            connection += viewModel.connectFilterState(filterState, attribute, FilterOperator.Or)
            connection += viewModel.connectSearcher(searcher, attribute)
            connection += viewModel.connectView(view, presenter)

            searcher.searchAsync()
        }

        override fun onDestroy() {
            super.onDestroy()
            connection.disconnect()
            searcher.cancel()
        }
    }
}