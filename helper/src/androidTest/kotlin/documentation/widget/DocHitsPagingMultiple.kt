package documentation.widget

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.algolia.instantsearch.helper.android.list.SearcherMultipleIndexDataSource
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.algolia.search.model.multipleindex.IndexQuery
import kotlinx.serialization.Serializable
import org.junit.Ignore


@Ignore
class DocHitsPagingMultiple {

    class MyActivity : AppCompatActivity() {

        val client = ClientSearch(
            ApplicationID("YourApplicationID"),
            APIKey("YourAPIKey")
        )
        val searcher = SearcherMultipleIndex(
            client,
            listOf(
                IndexQuery(IndexName("YourIndexName_A")),
                IndexQuery(IndexName("YourIndexName_B"))
            )
        )
        val pagedListConfig = PagedList.Config.Builder().setPageSize(10).build()
        val itemAFactory = SearcherMultipleIndexDataSource.Factory(searcher, 0, MyItemA.serializer())
        val itemBFactory = SearcherMultipleIndexDataSource.Factory(searcher, 1, MyItemB.serializer())
        val itemsA = LivePagedListBuilder<Int, MyItemA>(itemAFactory, pagedListConfig).build()
        val itemsB = LivePagedListBuilder<Int, MyItemB>(itemBFactory, pagedListConfig).build()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            itemsA.observe(this, Observer { hits ->
                // Use updated Hits here
            })
            itemsB.observe(this, Observer { hits ->
                // Use updated Hits here
            })

            searcher.searchAsync()
        }

        override fun onDestroy() {
            super.onDestroy()
            searcher.cancel()
        }
    }

    @Serializable
    data class MyItemA(
        val title: String
    )

    @Serializable
    data class MyItemB(
        val title: String
    )
}