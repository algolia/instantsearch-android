package com.algolia.instantsearch.demo.guide

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.helper.android.searchbox.connectView


class SampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sample_activity)
        val viewModel = ViewModelProviders.of(this)[SampleViewModel::class.java]

        viewModel.searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}