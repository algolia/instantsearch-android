package event

import com.algolia.instantsearch.core.event.Callback
import com.algolia.instantsearch.core.event.EventView
import com.algolia.instantsearch.core.event.EventViewModelImpl
import com.algolia.instantsearch.core.event.connectView
import shouldBeFalse
import shouldBeTrue
import shouldNotBeNull
import kotlin.test.Test


class TestEventConnectView {

    private class MockUnitEventView : EventView<Unit> {

        override var onClick: Callback<Unit>? = null
    }

    private class MockBooleanEventView : EventView<Boolean> {

        override var onClick: Callback<Boolean>? = null
    }

    @Test
    fun onClickUnitShouldCallOnTriggeredUnit() {
        val viewModel = EventViewModelImpl<Unit>()
        val view = MockUnitEventView()
        var clicked = false

        viewModel.onTriggered += { clicked = true }
        clicked.shouldBeFalse()
        viewModel.connectView(view) { viewModel.trigger(Unit) }
        view.onClick.shouldNotBeNull()
        view.onClick!!.invoke(Unit)
        clicked.shouldBeTrue()
    }

    @Test
    fun onClickWithParamShouldCallOnTriggeredWithParam() {
        val viewModel = EventViewModelImpl<Boolean>()
        val view = MockBooleanEventView()
        var clicked = false

        viewModel.onTriggered += { clicked = it }
        clicked.shouldBeFalse()
        viewModel.connectView(view) { viewModel.trigger(it) }
        view.onClick.shouldNotBeNull()
        view.onClick!!.invoke(true)
        clicked.shouldBeTrue()
    }
}