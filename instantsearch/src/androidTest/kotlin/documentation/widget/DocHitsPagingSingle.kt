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
import com.algolia.instantsearch.helper.android.list.SearcherSingleIndexDataSource
import com.algolia.instantsearch.helper.filter.state.FilterState
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
        val dataSourceFactory = SearcherSingleIndexDataSource.Factory(searcher) { it.deserialize(Movie.serializer()) }
        val pagedListConfig = PagedList.Config.Builder()
            .setPageSize(10) // configure according to your needs
            .build()
        val movies = LivePagedListBuilder<Int, Movie>(dataSourceFactory, pagedListConfig).build()
        val filterState = FilterState()
        val adapter = MovieAdapter()
        val connection = ConnectionHandler()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            connection += filterState.connectPagedList(movies)

            movies.observe(this, Observer { hits -> adapter.submitList(hits) })

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
        val title: String,
    )

    class MovieViewHolder(val view: TextView) : RecyclerView.ViewHolder(view) {

        fun bind(data: Movie) {
            view.text = data.title
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
                newItem: Movie,
            ): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(
                oldItem: Movie,
                newItem: Movie,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
