package documentation.widget

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.helper.android.filter.clear.FilterClearViewImpl
import com.algolia.instantsearch.helper.filter.clear.FilterClearView
import com.algolia.instantsearch.helper.filter.clear.FilterClearViewModel
import com.algolia.instantsearch.helper.filter.clear.connectFilterState
import com.algolia.instantsearch.helper.filter.clear.connectView
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.instantsearch.helper.searcher.hits.HitsSearcher
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import org.junit.Ignore

@Ignore
internal class DocClearFilters {

    class MyActivity : AppCompatActivity() {

        val client = ClientSearch(
            ApplicationID("YourApplicationID"),
            APIKey("YourAPIKey")
        )
        val searcher = HitsSearcher(client, IndexName("YourIndexName"))
        val filterState = FilterState()
        val viewModel = FilterClearViewModel()
        val connection = ConnectionHandler()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            val someView = View(this)
            val view: FilterClearView =
                FilterClearViewImpl(someView)

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
