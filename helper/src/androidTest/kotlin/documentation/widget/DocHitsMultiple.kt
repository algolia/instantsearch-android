package documentation.widget

import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.helper.android.list.SearcherMultipleIndexDataSource
import com.algolia.instantsearch.helper.android.list.SearcherSingleIndexDataSource
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.algolia.search.model.multipleindex.IndexQuery
import kotlinx.serialization.Serializable
import org.junit.Ignore


@Ignore
class DocHitsMultiple {

    class MyActivity : AppCompatActivity() {

        val client = ClientSearch(
            ApplicationID("YourApplicationID"),
            APIKey("YourAPIKey")
        )
        val index = client.initIndex(IndexName("YourIndexName"))
        private val searcher = SearcherMultipleIndex(
            client,
            listOf(
                IndexQuery(IndexName("YourIndexName_A")),
                IndexQuery(IndexName("YourIndexName_B"))
            )
        )
        private val pagedListConfig = PagedList.Config.Builder().setPageSize(10).build()
        private val itemAFactory = SearcherMultipleIndexDataSource.Factory(searcher, 0, MyItemA.serializer())
        private val itemBFactory = SearcherMultipleIndexDataSource.Factory(searcher, 1, MyItemB.serializer())
        private val movies = LivePagedListBuilder<Int, MyItemA>(itemAFactory, pagedListConfig).build()
        private val actors = LivePagedListBuilder<Int, MyItemB>(itemBFactory, pagedListConfig).build()
        val adapter = DocHits.MyAdapter()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            actors.observe(this, Observer { hits ->  })
            movies.observe(this, Observer { hits ->  })

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