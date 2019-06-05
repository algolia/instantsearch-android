package event

import com.algolia.instantsearch.core.event.EventView
import com.algolia.instantsearch.core.event.EventViewModelImpl
import com.algolia.instantsearch.core.event.connectView
import shouldBeFalse
import shouldBeTrue
import shouldNotBeNull
import kotlin.test.Test


class TestFilterClearConnectView {

    private class MockFilterClearView : EventView<Unit> {

        override var onClick: ((Unit) -> Unit)? = null
    }

    @Test
    fun onClickShouldCallOnTriggered() {
        val viewModel = EventViewModelImpl<Unit>()
        val view = MockFilterClearView()
        var clicked = false

        viewModel.onTriggered += { clicked = true }
        clicked.shouldBeFalse()
        viewModel.connectView(view) { viewModel.trigger(Unit) }
        view.onClick.shouldNotBeNull()
        view.onClick?.invoke(Unit)
        clicked.shouldBeTrue()
    }
}
