package com.algolia.instantsearch.helper.android.searchbox

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.algolia.instantsearch.helper.searchbox.SearchBoxView

sealed class SearchBox(internal val view: View) : SearchBoxView {
    internal var submitListener: ((String?) -> Unit)? = null
    internal var changeListener: ((String?) -> Unit)? = null

    abstract class SearchView(searchView: View) : SearchBox(searchView) {

        class System(private val searchView: android.widget.SearchView) : SearchBox.SearchView(searchView) {

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

        class Support(private val searchView: androidx.appcompat.widget.SearchView) :
            SearchBox.SearchView(searchView) {

            override fun setTextChangeListener(listener: (String?) -> Unit) {
                changeListener = listener
                setSearchViewListener()
            }

            override fun setTextSubmitListener(listener: (String?) -> Unit) {
                submitListener = listener
                setSearchViewListener()
            }

            private fun setSearchViewListener() {
                searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
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

    abstract class EditText(editText: View) : SearchBox(editText) {

        internal val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                changeListener?.invoke(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        }

        class System(private val editText: android.widget.EditText) : EditText(editText) {

            override fun setTextChangeListener(listener: (String?) -> Unit) {
                editText.removeTextChangedListener(textWatcher)
                changeListener = listener
                editText.addTextChangedListener(textWatcher)
            }

            override fun setTextSubmitListener(listener: (String?) -> Unit) = Unit
        }

        class Support(private val editText: androidx.appcompat.widget.AppCompatEditText) : EditText(editText) {

            override fun setTextChangeListener(listener: (String?) -> Unit) {
                editText.removeTextChangedListener(textWatcher)
                changeListener = listener
                editText.addTextChangedListener(textWatcher)
            }

            override fun setTextSubmitListener(listener: (String?) -> Unit) = Unit
        }
    }
}