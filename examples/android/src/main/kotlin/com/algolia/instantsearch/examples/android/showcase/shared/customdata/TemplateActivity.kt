package com.algolia.instantsearch.examples.android.showcase.shared.customdata

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.algolia.instantsearch.examples.android.showcase.compose.ui.ShowcaseTheme
import com.algolia.instantsearch.examples.android.showcase.compose.ui.component.TitleTopBar

class TemplateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val text = intent.getStringExtra(EXTRA_CONTENT) ?: ""
        setContent {
            ShowcaseTheme {
                Scaffold(
                    topBar = {
                        TitleTopBar()
                    },
                    content = { padding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = text,
                                style = MaterialTheme.typography.h5
                            )
                        }
                    }
                )
            }
        }
    }

    companion object {
        const val EXTRA_CONTENT = "TEMPLATE_CONTENT"
    }
}
