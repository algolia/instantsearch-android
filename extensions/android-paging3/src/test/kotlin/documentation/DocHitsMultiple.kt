package documentation

import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagingConfig
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.ExperimentalInstantSearch
import com.algolia.instantsearch.android.paging3.Paginator
import com.algolia.instantsearch.android.paging3.filterstate.connectPaginator
import com.algolia.instantsearch.android.paging3.liveData
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.hits.addHitsSearcher
import com.algolia.instantsearch.helper.searcher.multi.MultiSearcher
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import kotlinx.serialization.Serializable
import org.junit.Ignore

@Ignore
@OptIn(ExperimentalInstantSearch::class)
internal class DocHitsMultiple {

    class MyActivity : AppCompatActivity() {

        val client = ClientSearch(
            ApplicationID("YourApplicationID"),
            APIKey("YourAPIKey")
        )
        val multiSearcher = MultiSearcher(client)
        val searcherMovie = multiSearcher.addHitsSearcher(IndexName("IndexMovie"))
        val searcherActor = multiSearcher.addHitsSearcher(IndexName("IndexActor"))

        val pagingConfig = PagingConfig(pageSize = 10, enablePlaceholders = false)
        val moviesPaginator = Paginator(searcherMovie, pagingConfig) { it.deserialize(Movie.serializer()) }
        val actorsPaginator = Paginator(searcherActor, pagingConfig) { it.deserialize(Actor.serializer()) }

        val adapterMovie = MovieAdapter()
        val adapterActor = ActorAdapter()
        val filterState = FilterState()
        val connection = ConnectionHandler()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            connection += filterState.connectPaginator(moviesPaginator)
            connection += filterState.connectPaginator(actorsPaginator)

            actorsPaginator.liveData.observe(this) { adapterActor.submitData(lifecycle, it) }
            moviesPaginator.liveData.observe(this) { adapterMovie.submitData(lifecycle, it) }

            multiSearcher.searchAsync()
        }

        override fun onDestroy() {
            super.onDestroy()
            multiSearcher.cancel()
            connection.disconnect()
        }
    }

    @Serializable
    data class Movie(
        val title: String,
    )

    @Serializable
    data class Actor(
        val name: String,
    )

    class MovieViewHolder(val view: TextView) : RecyclerView.ViewHolder(view) {

        fun bind(data: Movie) {
            view.text = data.title
        }
    }

    class ActorViewHolder(val view: TextView) : RecyclerView.ViewHolder(view) {

        fun bind(data: Actor) {
            view.text = data.name
        }
    }

    class ActorAdapter : PagingDataAdapter<Actor, ActorViewHolder>(ActorAdapter) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
            return ActorViewHolder(TextView(parent.context))
        }

        override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
            getItem(position)?.let { actor -> holder.bind(actor) }
        }

        companion object : DiffUtil.ItemCallback<Actor>() {
            override fun areItemsTheSame(oldItem: Actor, newItem: Actor) = oldItem.name == newItem.name
            override fun areContentsTheSame(oldItem: Actor, newItem: Actor) = oldItem == newItem
        }
    }

    class MovieAdapter : PagingDataAdapter<Movie, MovieViewHolder>(MovieAdapter) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
            return MovieViewHolder(TextView(parent.context))
        }

        override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
            getItem(position)?.let { movie -> holder.bind(movie) }
        }

        companion object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem.title == newItem.title
            override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem
        }
    }
}
