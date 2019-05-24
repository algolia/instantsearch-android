package filter.clear

import com.algolia.instantsearch.helper.filter.clear.ClearFilterView

class MockClearFilterView : ClearFilterView {
    override var onClick: (() -> Unit)? = null
}