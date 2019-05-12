package searcher

import androidx.appcompat.widget.SearchView


fun SearcherSingleIndex.connectSearchView(searchView: SearchView) {
    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(newText: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            query.query = newText
            search()
            return true
        }
    })
}