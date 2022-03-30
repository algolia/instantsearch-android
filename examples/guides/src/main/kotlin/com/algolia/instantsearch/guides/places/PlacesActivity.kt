package com.algolia.instantsearch.guides.places

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.guides.R
import com.algolia.instantsearch.guides.extension.configure
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searchbox.connectView
import com.algolia.instantsearch.searcher.SearcherPlaces
import com.algolia.search.client.ClientPlaces
import com.algolia.search.configuration.ConfigurationPlaces
import com.algolia.search.model.places.Country
import com.algolia.search.model.places.PlaceType
import com.algolia.search.model.places.PlacesQuery
import com.algolia.search.model.search.Language
import io.ktor.client.features.logging.*

class PlacesActivity : AppCompatActivity() {

    val query = PlacesQuery(
        type = PlaceType.City,
        hitsPerPage = 10,
        aroundLatLngViaIP = false,
        countries = listOf(Country.France)
    )
    val client: ClientPlaces = ClientPlaces(ConfigurationPlaces(logLevel = LogLevel.ALL))
    val searcher = SearcherPlaces(client = client, query = query, language = Language.English)
    val searchBox = SearchBoxConnector(searcher)
    val adapter = Adapter()
    val connection = ConnectionHandler(searchBox)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places)

        val searchView = findViewById<SearchView>(R.id.searchView)
        connection += searchBox.connectView(SearchBoxViewAppCompat(searchView))
        connection += searcher.connectHitsView(adapter) { hits -> hits.hits }

        findViewById<RecyclerView>(R.id.placesList).configure(adapter)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        connection.clear()
    }
}
