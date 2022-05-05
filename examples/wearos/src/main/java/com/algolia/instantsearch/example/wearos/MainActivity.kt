package com.algolia.instantsearch.example.wearos

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import coil.load
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.searcher.connectView
import com.algolia.search.helper.deserialize

class MainActivity : ComponentActivity() {

    private val showsViewModel: ShowsViewModel by viewModels()
    private val connections = ConnectionHandler()

    private val startSpeech =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val spokenText: String? = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.first()
                if (spokenText != null) navigateToSearch(spokenText)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<ImageButton>(R.id.microphone).setOnClickListener {
            displaySpeechRecognizer()
        }

        connections += showsViewModel.searcher.connectView(view = ::mostSearchedShows) {
            it?.hits?.deserialize(Show.serializer()) ?: emptyList()
        }
        showsViewModel.searcher.query.hitsPerPage = 4
        showsViewModel.search("")
    }

    private fun mostSearchedShows(shows: List<Show>) {
        listOf<ImageView>(
            findViewById(R.id.up_left),
            findViewById(R.id.up_right),
            findViewById(R.id.down_left),
            findViewById(R.id.down_right)
        ).zip(shows).forEach { (imageView, show) -> poster(imageView, show.posterUrl) }
    }

    private fun poster(imageView: ImageView, posterUrl: String) {
        imageView.load(posterUrl) {
            transformations(RoundedCornersTransformation(), GrayscaleTransformation(0.10f))
            scale(Scale.FILL)
        }
    }

    // Create an intent that can start the Speech Recognizer activity
    private fun displaySpeechRecognizer() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }
        startSpeech.launch(intent)
    }

    private fun navigateToSearch(query: String) {
        val intent = Intent(this, ShowsActivity::class.java)
        intent.putExtra(ShowsActivity.ExtraQuery, query)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        connections.disconnect()
    }
}
