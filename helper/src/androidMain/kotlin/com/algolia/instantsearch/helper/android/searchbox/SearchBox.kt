package com.algolia.instantsearch.helper.android.searchbox

import android.view.View
import androidx.appcompat.widget.SearchView
import com.algolia.instantsearch.helper.searchbox.SearchBoxView

sealed class SearchBox(internal val view: View) : SearchBoxView {
    internal var submitListener: ((String?) -> Unit)? = null
    internal var changeListener: ((String?) -> Unit)? = null

    class System(val searchView: android.widget.SearchView) : SearchBox(searchView) {
        override fun setTextChangeListener(listener: (String?) -> Unit) {
            changeListener = listener
            setSearchViewListener()
        }

        override fun setTextSubmitListener(listener: (String?) -> Unit) {
            submitListener = listener
            setSearchViewListener()
        }

        private fun setSearchViewListener() {
            searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    submitListener?.let {
                        it(query)
                        return true
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    changeListener?.let {
                        it(newText)
                        return true
                    }
                    return false
                }
            })
        }
    }

    class Support(val searchView: SearchView) : SearchBox(searchView) {
        override fun setTextChangeListener(listener: (String?) -> Unit) {
            changeListener = listener
            setSearchViewListener()
        }

        override fun setTextSubmitListener(listener: (String?) -> Unit) {
            submitListener = listener
            setSearchViewListener()
        }

        private fun setSearchViewListener() {
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    submitListener?.let {
                        it(query)
                        return true
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    changeListener?.let {
                        it(newText)
                        return true
                    }
                    return false
                }
            })
        }
    }
}