package com.algolia.instantsearch.guides.voice

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.algolia.instantsearch.guides.R
import com.algolia.instantsearch.voice.VoiceSpeechRecognizer
import com.algolia.instantsearch.voice.ui.Voice
import com.algolia.instantsearch.voice.ui.Voice.isRecordAudioPermissionGranted
import com.algolia.instantsearch.voice.ui.Voice.shouldExplainPermission
import com.algolia.instantsearch.voice.ui.Voice.showPermissionRationale
import com.algolia.instantsearch.voice.ui.VoiceInputDialogFragment
import com.algolia.instantsearch.voice.ui.VoicePermissionDialogFragment
import java.util.*

class VoiceSearchActivity : AppCompatActivity(), VoiceSpeechRecognizer.ResultsListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice_search)

        findViewById<Button>(R.id.buttonVoice).setOnClickListener { _ ->
            if (!isRecordAudioPermissionGranted()) {
                VoicePermissionDialogFragment().show(supportFragmentManager, Tag.Permission.name)
            } else {
                showVoiceDialog()
            }
        }

        findViewById<Button>(R.id.buttonPermission).setOnClickListener {
            VoicePermissionDialogFragment().show(supportFragmentManager, Tag.Permission.name)
        }
    }

    override fun onResults(possibleTexts: Array<out String>) {
        findViewById<TextView>(R.id.results).text = possibleTexts.firstOrNull()
            ?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
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
        val voiceInputDialogFragment = getVoiceDialog() ?: VoiceInputDialogFragment()
        voiceInputDialogFragment.setSuggestions("Phone case", "Running shoes")
        supportFragmentManager.commit {
            add(voiceInputDialogFragment, Tag.Voice.name)
        }
    }

    private fun getVoiceDialog() =
        supportFragmentManager.findFragmentByTag(Tag.Voice.name) as? VoiceInputDialogFragment

    private fun getPermissionDialog() =
        supportFragmentManager.findFragmentByTag(Tag.Permission.name) as? VoicePermissionDialogFragment

    private fun getPermissionView(): View = requireNotNull(getPermissionDialog())
        .requireView()
        .findViewById(R.id.positive)

    private enum class Tag {
        Permission,
        Voice
    }
}