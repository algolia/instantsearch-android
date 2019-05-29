package filter.clear

import com.algolia.instantsearch.helper.filter.clear.ClearFiltersView

class MockClearFiltersView : ClearFiltersView {
    override var onClick: (() -> Unit)? = null
}