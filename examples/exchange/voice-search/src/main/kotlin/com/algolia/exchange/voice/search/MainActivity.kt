package com.algolia.exchange.voice.search

import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import com.algolia.instantsearch.voice.VoiceSpeechRecognizer
import com.algolia.instantsearch.voice.ui.Voice
import com.algolia.instantsearch.voice.ui.Voice.isRecordAudioPermissionGranted
import com.algolia.instantsearch.voice.ui.Voice.shouldExplainPermission
import com.algolia.instantsearch.voice.ui.Voice.showPermissionRationale
import com.algolia.instantsearch.voice.ui.VoiceInputDialogFragment
import com.algolia.instantsearch.voice.ui.VoicePermissionDialogFragment

class MainActivity : AppCompatActivity(), VoiceSpeechRecognizer.ResultsListener {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                SearchScreen(
                    searchBoxState = viewModel.searchBoxState,
                    productsState = viewModel.productsState,
                    onMicClick = ::onMicClick
                )
            }
        }
    }

    private fun onMicClick() {
        if (!isRecordAudioPermissionGranted()) {
            VoicePermissionDialogFragment().show(supportFragmentManager, Tag.Permission.name)
        } else {
            showVoiceDialog()
        }
    }

    override fun onResults(possibleTexts: Array<out String>) {
        viewModel.setQuery(possibleTexts.firstOrNull())
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (Voice.isRecordPermissionWithResults(requestCode, grantResults)) {
            when {
                Voice.isPermissionGranted(grantResults) -> showVoiceDialog()
                shouldExplainPermission() -> showPermissionRationale(getPermissionView())
                else -> Voice.showPermissionManualInstructions(getPermissionView())
            }
        }
    }


    private fun showVoiceDialog() {
        getPermissionDialog()?.dismiss()
        (getVoiceDialog() ?: VoiceInputDialogFragment()).apply {
            setSuggestions("Phone case", "Smartwatch")
            show(supportFragmentManager, Tag.Voice.name)
        }
    }

    private fun getVoiceDialog() =
        (supportFragmentManager.findFragmentByTag(Tag.Voice.name) as? VoiceInputDialogFragment)

    private fun getPermissionDialog() =
        (supportFragmentManager.findFragmentByTag(Tag.Permission.name) as? VoicePermissionDialogFragment)

    private fun getPermissionView(): View =
        getPermissionDialog()!!.requireView().findViewById(R.id.positive)


    private enum class Tag {
        Permission, Voice
    }
}
