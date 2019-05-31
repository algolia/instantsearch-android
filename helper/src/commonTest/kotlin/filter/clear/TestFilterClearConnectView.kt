package filter.clear

import com.algolia.instantsearch.helper.filter.clear.FilterClearView
import com.algolia.instantsearch.helper.filter.clear.FilterClearViewModel
import com.algolia.instantsearch.helper.filter.clear.connectView
import shouldBeFalse
import shouldBeTrue
import shouldNotBeNull
import kotlin.test.Test


class TestFilterClearConnectView {

    @Test
    fun connectSetsOnClick() {
        var clicked = false
        val viewModel = FilterClearViewModel()
        val view = object : FilterClearView {
            override var onClick: ((Unit) -> Unit)? = null
        }

        viewModel.onClicked += { clicked = true }
        viewModel.connectView(view)
        clicked.shouldBeFalse()
        view.onClick.shouldNotBeNull()
        view.onClick?.invoke(Unit)
        clicked.shouldBeTrue()
    }

}
