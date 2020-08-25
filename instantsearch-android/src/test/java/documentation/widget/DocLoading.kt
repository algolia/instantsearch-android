package documentation.widget

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.loading.LoadingView
import com.algolia.instantsearch.core.loading.LoadingViewModel
import com.algolia.instantsearch.core.loading.connectView
import com.algolia.instantsearch.helper.android.loading.LoadingViewSwipeRefreshLayout
import com.algolia.instantsearch.helper.loading.connectSearcher
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import org.junit.Ignore


@Ignore
class DocLoading {

    class MyActivity: AppCompatActivity() {

        val client = ClientSearch(
            ApplicationID("YourApplicationID"),
            APIKey("YourAPIKey")
        )
        val index = client.initIndex(IndexName("YourIndexName"))
        val searcher = SearcherSingleIndex(index)
        val viewModel = LoadingViewModel()
        val connection = ConnectionHandler()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            val swipeRefreshLayout = SwipeRefreshLayout(this)
            val view: LoadingView = LoadingViewSwipeRefreshLayout(swipeRefreshLayout)

            connection += viewModel.connectSearcher(searcher)
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
