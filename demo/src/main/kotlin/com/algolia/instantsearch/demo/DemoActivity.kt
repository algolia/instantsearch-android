package com.algolia.instantsearch.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.IndexName

abstract class DemoActivity : AppCompatActivity() {
    internal val searcher = SearcherSingleIndex(client.initIndex(IndexName("stub")))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        searcher.index = client.initIndex(intent.indexName)
    }
}