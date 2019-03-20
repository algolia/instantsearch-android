package com.algolia.instantsearch.android.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.algolia.instantsearch.android.StatsWidget
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.algolia.search.model.search.Query
import android.widget.Toast.LENGTH_LONG
import searcher.SearcherSingleIndex

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val view = findViewById<TextView>(R.id.text)
        val stats = StatsWidget(view)
        val client = ClientSearch(ApplicationID("latency"), APIKey("3cfd1f8bfa88c7709f6bacf8203194e8"))
        val index = client.initIndex(IndexName("products_android_demo"))
        val searcher = SearcherSingleIndex(index, Query(""))
        searcher.listeners += {
            stats.updateView(it)
            this@MainActivity.runOnUiThread { Toast.makeText(this, it.toString(), LENGTH_LONG).show() }
        }
        searcher.search()
    }
}
