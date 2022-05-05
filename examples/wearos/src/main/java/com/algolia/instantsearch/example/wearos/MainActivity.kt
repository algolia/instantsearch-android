package com.algolia.instantsearch.example.wearos

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.wear.widget.WearableLinearLayoutManager
import androidx.wear.widget.WearableRecyclerView
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.search.client.ClientSearch
import com.algolia.search.helper.deserialize
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import io.ktor.client.features.logging.LogLevel

class MainActivity : Activity() {

    private val client = ClientSearch(
        ApplicationID("latency"),
        APIKey("3832e8fcaf80b1c7085c59fa3e4d266d"),
        LogLevel.ALL
    )
    private val searcher = HitsSearcher(client, IndexName("tmdb_movies_shows"))
    private val connections = ConnectionHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.text).visibility = View.GONE
        findViewById<ImageButton>(R.id.microphone).visibility = View.GONE

        //findViewById<ImageButton>(R.id.microphone).setOnClickListener {
        //    displaySpeechRecognizer()
        //}

        val movieAdapter = MovieAdapter()
        connections += searcher.connectHitsView(movieAdapter) {
            it.hits.deserialize(Movie.serializer())
        }

        // curved layout
        findViewById<WearableRecyclerView>(R.id.list).apply {
            adapter = movieAdapter
            layoutManager = WearableLinearLayoutManager(this@MainActivity, CustomScrollingLayoutCallback())
        }

        searcher.query.attributesToRetrieve = Movie.attributes
        searcher.searchAsync()
    }

    // Create an intent that can start the Speech Recognizer activity
    private fun displaySpeechRecognizer() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }
        startActivityForResult(intent, SPEECH_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            val spokenText: String? =
                data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.first()
            // Do something with spokenText.
            if (spokenText != null) search(spokenText)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun search(query: String) {
        findViewById<TextView>(R.id.text).visibility = View.GONE
        findViewById<ImageButton>(R.id.microphone).visibility = View.GONE
        searcher.setQuery(query)
        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        connections.clear()
    }

    companion object {
        private const val SPEECH_REQUEST_CODE = 0
    }
}
