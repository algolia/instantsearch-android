package documentation.widget

import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.client.ClientSearch
import com.algolia.search.helper.deserialize
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import kotlinx.serialization.Serializable
import org.junit.Ignore


@Ignore
class DocHits {

    class MyActivity : AppCompatActivity() {

        val client = ClientSearch(
            ApplicationID("YourApplicationID"),
            APIKey("YourAPIKey")
        )
        val index = client.initIndex(IndexName("YourIndexName"))
        val searcher = SearcherSingleIndex(index)
        val connection = ConnectionHandler()
        val adapter = MyAdapter()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            connection += searcher.connectHitsView(adapter) { response ->
                response.hits.deserialize(MyItem.serializer())
            }
            searcher.searchAsync()
        }

        override fun onDestroy() {
            super.onDestroy()
            connection.disconnect()
            searcher.cancel()
        }
    }

    @Serializable
    data class MyItem(
        val title: String
    )

    class MyAdapter : RecyclerView.Adapter<MyViewHolder>(), HitsView<MyItem> {

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

        override fun getItemCount(): Int {
            return items.size
        }
    }

    class MyViewHolder(val view: TextView): RecyclerView.ViewHolder(view) {

        fun bind(data: MyItem) {
            view.text = data.title
        }
    }
}