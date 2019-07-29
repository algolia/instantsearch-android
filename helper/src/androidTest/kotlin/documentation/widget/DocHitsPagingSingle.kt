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
import com.algolia.instantsearch.helper.android.list.SearcherSingleIndexDataSource
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import kotlinx.serialization.Serializable
import org.junit.Ignore


@Ignore
class DocHitsPagingSingle {

    class MyActivity : AppCompatActivity() {

        val client = ClientSearch(
            ApplicationID("YourApplicationID"),
            APIKey("YourAPIKey")
        )
        val index = client.initIndex(IndexName("YourIndexName"))
        val searcher = SearcherSingleIndex(index)
        val dataSourceFactory = SearcherSingleIndexDataSource.Factory(searcher, MyItem.serializer())
        val pagedListConfig = PagedList.Config.Builder().setPageSize(10).build()
        val movies = LivePagedListBuilder<Int, MyItem>(dataSourceFactory, pagedListConfig).build()
        val adapter = MyAdapter()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            movies.observe(this, Observer { hits -> adapter.setHits(hits) })
            searcher.searchAsync()
        }

        override fun onDestroy() {
            super.onDestroy()
            searcher.cancel()
        }
    }

    @Serializable
    data class MyItem(
        val title: String
    )

    class MyAdapter : ListAdapter<MyItem, MyViewHolder>(MyAdapter), HitsView<MyItem> {

        private var items: List<MyItem> = listOf()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            return MyViewHolder(TextView(parent.context))
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val item = items[position]

            holder.bind(item)
        }

        override fun setHits(hits: List<MyItem>) {

            items = hits
            notifyDataSetChanged()
        }

        companion object : DiffUtil.ItemCallback<MyItem>() {

            override fun areItemsTheSame(
                oldItem: MyItem,
                newItem: MyItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: MyItem,
                newItem: MyItem
            ): Boolean {
                return oldItem.title == newItem.title
            }
        }
    }

    class MyViewHolder(val view: TextView) : RecyclerView.ViewHolder(view) {

        fun bind(data: MyItem) {
            view.text = data.title
        }
    }
}