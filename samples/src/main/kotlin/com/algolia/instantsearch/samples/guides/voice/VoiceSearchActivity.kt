package com.algolia.instantsearch.samples.guides.voice

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.algolia.instantsearch.samples.R
import com.algolia.instantsearch.voice.VoiceSpeechRecognizer
import com.algolia.instantsearch.voice.ui.Voice
import com.algolia.instantsearch.voice.ui.Voice.isRecordAudioPermissionGranted
import com.algolia.instantsearch.voice.ui.Voice.shouldExplainPermission
import com.algolia.instantsearch.voice.ui.Voice.showPermissionRationale
import com.algolia.instantsearch.voice.ui.VoiceInputDialogFragment
import com.algolia.instantsearch.voice.ui.VoicePermissionDialogFragment

class VoiceSearchActivity : AppCompatActivity(), VoiceSpeechRecognizer.ResultsListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice_search)

        val buttonVoice = findViewById<Button>(R.id.buttonVoice)
        buttonVoice.setOnClickListener { onMicClick() }

        val buttonPermission = findViewById<Button>(R.id.buttonPermission)
        buttonPermission.setOnClickListener { showPermissionDialog() }
    }

    private fun onMicClick() {
        if (isRecordAudioPermissionGranted()) showVoiceDialog() else showPermissionDialog()
    }

    private fun showPermissionDialog() {
        VoicePermissionDialogFragment().show(supportFragmentManager, Tag.Permission.name)
    }

    override fun onResults(possibleTexts: Array<out String>) {
        val results = findViewById<TextView>(R.id.results)
        results.text = possibleTexts.firstOrNull()
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
        val voiceInputDialogFragment = getVoiceDialog() ?: VoiceInputDialogFragment()
        voiceInputDialogFragment.setSuggestions("Phone case", "Running shoes")
        supportFragmentManager.commit {
            add(voiceInputDialogFragment, Tag.Voice.name)
        }
    }

    private fun getVoiceDialog(): VoiceInputDialogFragment? {
        return supportFragmentManager.findFragmentByTag(Tag.Voice.name) as? VoiceInputDialogFragment
    }

    private fun getPermissionDialog(): VoicePermissionDialogFragment? {
        return supportFragmentManager.findFragmentByTag(Tag.Permission.name) as? VoicePermissionDialogFragment
    }

    private fun getPermissionView(): View {
        return getPermissionDialog()?.requireView()?.findViewById(R.id.positive) ?: error("permission view not found")
    }

    private enum class Tag {
        Permission, Voice
    }
}
