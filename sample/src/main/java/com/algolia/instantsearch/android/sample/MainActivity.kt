package com.algolia.instantsearch.android.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.algolia.instantsearch.android.StatsWidget
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.algolia.search.model.search.Query
import searcher.SearcherSingleQuery

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val view = findViewById<TextView>(R.id.text)
        val stats = StatsWidget(view)
        val client = ClientSearch(ApplicationID("***REMOVED***"), APIKey("26b270849a0b8189838581c341ff3b06"))
        val index = client.initIndex(IndexName("test"))
        val searcher = SearcherSingleQuery(index, Query(""))
        searcher.listeners += {
            stats.updateView(it)
            Toast(view.context).setText(it.toString())
        }
        searcher.search()
    }
}
