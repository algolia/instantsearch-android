package com.algolia.instantsearch.examples.android.codex.voice

import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import com.algolia.instantsearch.examples.android.R
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
        if (isRecordAudioPermissionGranted()) showVoiceDialog() else showPermissionDialog()
    }

    private fun showPermissionDialog() {
        VoicePermissionDialogFragment().show(supportFragmentManager, Tag.Permission.name)
    }

    override fun onResults(possibleTexts: Array<out String>) {
        viewModel.setQuery(possibleTexts.firstOrNull())
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
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
        val voiceDialog = getVoiceDialog() ?: VoiceInputDialogFragment()
        voiceDialog.setSuggestions("Phone case", "Running shoes")
        voiceDialog.show(supportFragmentManager, Tag.Voice.name)
    }

    private fun getVoiceDialog(): VoiceInputDialogFragment? {
        return supportFragmentManager.findFragmentByTag(Tag.Voice.name) as? VoiceInputDialogFragment
    }

    private fun getPermissionDialog(): VoicePermissionDialogFragment? {
        return supportFragmentManager.findFragmentByTag(Tag.Permission.name) as? VoicePermissionDialogFragment
    }

    private fun getPermissionView(): View {
        return getPermissionDialog()?.requireView()?.findViewById(com.algolia.instantsearch.voice.R.id.positive) ?: error("permission view not found")
    }

    private enum class Tag {
        Permission, Voice
    }
}
