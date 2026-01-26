package documentation.widget

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.algolia.instantsearch.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.searchbox.SearchBoxView
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.searchbox.connectView
import com.algolia.instantsearch.searchbox.connectSearcher
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.search.client.ClientSearch
import org.junit.Ignore

@Ignore
internal class DocSearchBox {

    class MyActivity : AppCompatActivity() {

        val client = ClientSearch(
            "YourApplicationID",
            "YourAPIKey"
        )
        val searcher = HitsSearcher(client, "YourIndexName")
        val viewModel = SearchBoxViewModel()
        val connection = ConnectionHandler()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            val someView = SearchView(this)
            val view: SearchBoxView = SearchBoxViewAppCompat(someView)

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
