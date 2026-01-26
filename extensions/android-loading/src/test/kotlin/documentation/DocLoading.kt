package documentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.algolia.instantsearch.android.loading.LoadingViewSwipeRefreshLayout
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.loading.LoadingView
import com.algolia.instantsearch.core.loading.LoadingViewModel
import com.algolia.instantsearch.core.loading.connectView
import com.algolia.instantsearch.loading.connectSearcher
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.search.client.ClientSearch
import org.junit.Ignore

@Ignore
internal class DocLoading {

    class MyActivity : AppCompatActivity() {

        val client = ClientSearch(
            "YourApplicationID",
            "YourAPIKey"
        )
        val searcher = HitsSearcher(client, "YourIndexName")
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
