package documentation.widget

import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.helper.android.filter.state.connectPagedList
import com.algolia.instantsearch.helper.android.list.SearcherMultipleIndexDataSource
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
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
        val indexMovie = IndexQuery(IndexName("IndexMovie"))
        val indexActor = IndexQuery(IndexName("IndexActor"))
        val index = client.initIndex(IndexName("YourIndexName"))
        val searcher = SearcherMultipleIndex(client, listOf(indexMovie, indexActor))
        val pagedListConfig = PagedList.Config.Builder().setPageSize(10).build()
        val movieFactory = SearcherMultipleIndexDataSource.Factory(searcher, indexMovie) {
            it.deserialize(Movie.serializer())
        }
        val actorFactory = SearcherMultipleIndexDataSource.Factory(searcher, indexActor) {
            it.deserialize(Actor.serializer())
        }
        val movies = LivePagedListBuilder(movieFactory, pagedListConfig).build()
        val actors = LivePagedListBuilder(actorFactory, pagedListConfig).build()
        val adapterMovie = MovieAdapter()
        val adapterActor = ActorAdapter()
        val filterState = FilterState()
        val connection = ConnectionHandler()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            connection += filterState.connectPagedList(actors)
            connection += filterState.connectPagedList(movies)

            actors.observe(this, Observer { hits -> adapterActor.submitList(hits) })
            movies.observe(this, Observer { hits -> adapterMovie.submitList(hits) })

            searcher.searchAsync()
        }

        override fun onDestroy() {
            super.onDestroy()
            searcher.cancel()
            connection.disconnect()
        }
    }

    @Serializable
    data class Movie(
        val title: String
    )

    @Serializable
    data class Actor(
        val name: String
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

    class ActorAdapter : PagedListAdapter<Actor, ActorViewHolder>(ActorAdapter) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
            return ActorViewHolder(TextView(parent.context))
        }

        override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
            val actor = getItem(position)

            if (actor != null) {
                holder.bind(actor)
            }
        }

        companion object : DiffUtil.ItemCallback<Actor>() {

            override fun areItemsTheSame(
                oldItem: Actor,
                newItem: Actor
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Actor,
                newItem: Actor
            ): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }

    class MovieAdapter : PagedListAdapter<Movie, MovieViewHolder>(MovieAdapter) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
            return MovieViewHolder(TextView(parent.context))
        }

        override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
            val movie = getItem(position)

            if (movie != null) holder.bind(movie)
        }

        companion object : DiffUtil.ItemCallback<Movie>() {

            override fun areItemsTheSame(
                oldItem: Movie,
                newItem: Movie
            ): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(
                oldItem: Movie,
                newItem: Movie
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
