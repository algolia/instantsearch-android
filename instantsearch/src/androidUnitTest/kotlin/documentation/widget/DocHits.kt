package documentation.widget

import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.algolia.client.api.SearchClient
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.search.helper.deserialize
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.jsonPrimitive
import org.junit.Ignore
import org.junit.Test

@Ignore
public class DocHits {

    @Test
    public fun json() {
        val client = SearchClient(
            "YourApplicationID",
            "YourAPIKey"
        )
        val searcher = HitsSearcher(client, "YourIndexName")
        val adapter = MovieAdapter()

        searcher.connectHitsView(adapter) { response ->
            response.hits.map { hit ->
                val title = hit.additionalProperties?.get("title")?.jsonPrimitive?.content ?: ""
                Movie(title)
            }
        }
    }

    public class MyActivity : AppCompatActivity() {

        public val client = SearchClient(
            "YourApplicationID",
            "YourAPIKey"
        )
        public val searcher: HitsSearcher = HitsSearcher(client, "YourIndexName")
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
        val title: String,
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
