package com.algolia.instantsearch.example.wearos

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : ComponentActivity() {

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
}
