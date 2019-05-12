package com.algolia.instantsearch.demo.home

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.algolia.instantsearch.android.searcher.connectSearchView
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.client
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.helper.deserialize
import com.algolia.search.model.IndexName
import kotlinx.android.synthetic.main.demo_home.*


class HomeDemo : AppCompatActivity() {

    private val index = client.initIndex(IndexName("mobile_demo_home"))
    private val searcher = SearcherSingleIndex(index)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_home)

        val hintIcon = ContextCompat.getDrawable(this, R.drawable.ic_search_hint)!!
        val hintText = getString(R.string.search_for_demos)

        setSupportActionBar(toolbar)
        searcher.connectSearchView(searchView)
        searchView.also {
            it.isSubmitButtonEnabled = false
            it.isFocusable = true
            it.setIconifiedByDefault(false)
            it.setOnQueryTextFocusChangeListener { _, hasFocus ->
                searchView.showQueryHintIcon(hasFocus, hintIcon, hintText)
            }
        }

        val adapter = HomeAdapter()

        directory.also {
            it.itemAnimator = null
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(this)
        }

        searcher.errorListeners += { throwable ->
            throwable.printStackTrace()
        }
        searcher.onResponseChanged += { response ->
            val hits = response.hits.deserialize(HomeHit.serializer())
                .groupBy { it.type }
                .flatMap { (key, value) ->
                    listOf(HomeItem.Header(key)) + value.map { HomeItem.Item(it) }.sortedBy { it.hit.objectID.raw }
                }

            adapter.submitList(hits)
        }
        searcher.search()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
    }

    private fun SearchView.showQueryHintIcon(showIconHint: Boolean, hintIcon: Drawable, hintText: String? = null) {
        queryHint = if (!showIconHint) {
            hintText
        } else {
            val textView = findViewById<AutoCompleteTextView>(R.id.search_src_text)
            val textSize = (textView.textSize * 1.25).toInt()

            hintIcon.setBounds(0, 0, textSize, textSize)
            SpannableStringBuilder("    ").also {
                it.setSpan(ImageSpan(hintIcon), 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                if (hintText != null) it.append(hintText) else it.append(" ")
            }
        }
    }
}