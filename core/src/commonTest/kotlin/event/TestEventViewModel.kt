package event

import com.algolia.instantsearch.core.event.EventViewModelImpl
import shouldBeFalse
import shouldBeTrue
import shouldEqual
import kotlin.test.Test


class TestEventViewModel {

    @Test
    fun click() {
        val viewModel = EventViewModelImpl<Unit>()
        var clicked = false

        viewModel.onTriggered += { clicked = true}
        clicked.shouldBeFalse()
        viewModel.trigger(Unit)
        clicked.shouldBeTrue()
    }

    @Test
    fun clickedStuff() {
        val viewModel = EventViewModelImpl<Int>()
        var clicked = 0

        viewModel.onTriggered += { clicked += it }
        clicked.shouldEqual(0)
        viewModel.trigger(1)
        viewModel.trigger(2)
        clicked.shouldEqual(3)
    }
}