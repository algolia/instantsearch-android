package documentation.widget

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.helper.android.sortby.SortByViewAutocomplete
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.sortby.SortByViewModel
import com.algolia.instantsearch.helper.sortby.connectSearcher
import com.algolia.instantsearch.helper.sortby.connectView
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import org.junit.Ignore

@Ignore
class DocSortBy {

    class MyActivity : AppCompatActivity() {

        val client = ClientSearch(
            ApplicationID("YourApplicationID"),
            APIKey("YourAPIKey")
        )
        val index = client.initIndex(IndexName("YourIndexName"))
        val indexAsc = client.initIndex(IndexName("YourIndexName_ASC"))
        val indexDesc = client.initIndex(IndexName("YourIndexName_DESC"))
        val searcher = SearcherSingleIndex(index)
        val indexes = mapOf(
            0 to index,
            1 to indexAsc,
            2 to indexDesc
        )
        val viewModel = SortByViewModel(indexes)
        val connection = ConnectionHandler()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            val autoCompleteTextView = AutoCompleteTextView(this)
            val adapter = ArrayAdapter<String>(this, R.layout.simple_list_item_1)
            val view = SortByViewAutocomplete(autoCompleteTextView, adapter)

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
