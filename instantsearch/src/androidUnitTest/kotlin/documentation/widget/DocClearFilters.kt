package documentation.widget

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.android.filter.clear.DefaultFilterClearView
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.filter.clear.FilterClearView
import com.algolia.instantsearch.filter.clear.FilterClearViewModel
import com.algolia.instantsearch.filter.clear.connectFilterState
import com.algolia.instantsearch.filter.clear.connectView
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.searcher.connectFilterState
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.search.client.ClientSearch
import org.junit.Ignore

@Ignore
internal class DocClearFilters {

    class MyActivity : AppCompatActivity() {

        val client = ClientSearch(
            "YourApplicationID",
            "YourAPIKey"
        )
        val searcher = HitsSearcher(client, "YourIndexName")
        val filterState = FilterState()
        val viewModel = FilterClearViewModel()
        val connection = ConnectionHandler()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            val someView = View(this)
            val view: FilterClearView =
                DefaultFilterClearView(someView)

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
