package com.algolia.instantsearch.guides.insights.extension

import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import com.algolia.instantsearch.guides.R

fun AppCompatActivity.configureSearchView(
    searchView: SearchView,
    queryHint: String
) {
    searchView.also {
        val hintIcon = ContextCompat.getDrawable(this, R.drawable.ic_search_hint)!!
        it.queryHint = queryHint
        it.setIconifiedByDefault(false)
        it.setOnQueryTextFocusChangeListener { _, hasFocus ->
            searchView.showQueryHintIcon(!hasFocus, hintIcon, queryHint)
        }
        searchView.showQueryHintIcon(true, hintIcon, queryHint)
    }
}

fun SearchView.showQueryHintIcon(
    showIconHint: Boolean,
    hintIcon: Drawable,
    hintText: String? = null
) {
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
