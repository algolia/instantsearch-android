package documentation.widget

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.android.filter.facet.FacetListAdapter
import com.algolia.instantsearch.android.filter.facet.FacetListViewHolder
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.filter.facet.DefaultFacetListPresenter
import com.algolia.instantsearch.filter.facet.FacetListView
import com.algolia.instantsearch.filter.facet.FacetListViewModel
import com.algolia.instantsearch.filter.facet.FacetSortCriterion
import com.algolia.instantsearch.filter.facet.connectFilterState
import com.algolia.instantsearch.filter.facet.connectSearcher
import com.algolia.instantsearch.filter.facet.connectView
import com.algolia.instantsearch.filter.state.FilterOperator
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.searcher.connectFilterState
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.search.Facet
import org.junit.Ignore

@Ignore
internal class DocFacetList {

    class MyActivity : AppCompatActivity() {

        class MyFacetListViewHolder(view: View) : FacetListViewHolder(view) {

            override fun bind(facet: Facet, selected: Boolean, onClickListener: View.OnClickListener) {
                // Bind your view
            }

            object Factory : FacetListViewHolder.Factory {

                override fun createViewHolder(parent: ViewGroup): FacetListViewHolder {
                    // Inflate your layout
                    val view = View(parent.context)

                    return MyFacetListViewHolder(view)
                }
            }
        }

        val client = ClientSearch(
            "YourApplicationID",
            "YourAPIKey"
        )
        val searcher = HitsSearcher(client, "YourIndexName")
        val filterState = FilterState()
        val attribute = "facetName"
        val viewModel = FacetListViewModel()
        val presenter = DefaultFacetListPresenter(listOf(FacetSortCriterion.CountDescending), limit = 5)
        val connection = ConnectionHandler()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            // Implement the FacetListView interface on the UI component of your choice.
            // For example, a RecyclerView.Adapter
            val view: FacetListView = FacetListAdapter(MyFacetListViewHolder.Factory)

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
