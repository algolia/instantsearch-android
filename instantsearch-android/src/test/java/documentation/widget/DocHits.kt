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
import com.algolia.search.client.Index
import com.algolia.search.helper.deserialize
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.jsonPrimitive
import org.junit.Ignore
import org.junit.Test

@Ignore
public class DocHits {

    @Test
    public fun json() {
        val client = ClientSearch(
            ApplicationID("YourApplicationID"),
            APIKey("YourAPIKey")
        )
        val index = client.initIndex(IndexName("YourIndexName"))
        val searcher = SearcherSingleIndex(index)
        val adapter = MovieAdapter()

        searcher.connectHitsView(adapter) { response ->
            response.hits.map { hit -> Movie(hit.json.getValue("title").jsonPrimitive.content) }
        }
    }

    public class MyActivity : AppCompatActivity() {

        public val client: ClientSearch = ClientSearch(
            ApplicationID("YourApplicationID"),
            APIKey("YourAPIKey")
        )
        public val index: Index = client.initIndex(IndexName("YourIndexName"))
        public val searcher: SearcherSingleIndex = SearcherSingleIndex(index)
        public val connection: ConnectionHandler = ConnectionHandler()
        public val adapter: MovieAdapter = MovieAdapter()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            connection += searcher.connectHitsView(adapter) { response ->
                response.hits.deserialize(Movie.serializer())
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
    public data class Movie(
        val title: String
    )

    public class MovieViewHolder(public val view: TextView) : RecyclerView.ViewHolder(view) {

        public fun bind(data: Movie) {
            view.text = data.title
        }
    }

    public class MovieAdapter : RecyclerView.Adapter<MovieViewHolder>(), HitsView<Movie> {

        private var movies: List<Movie> = listOf()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
            return MovieViewHolder(TextView(parent.context))
        }

        override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
            val movie = movies[position]

            holder.bind(movie)
        }

        override fun setHits(hits: List<Movie>) {
            movies = hits
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {
            return movies.size
        }
    }
}
