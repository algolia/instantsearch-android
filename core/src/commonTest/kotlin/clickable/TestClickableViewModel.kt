package clickable

import com.algolia.instantsearch.core.clickable.ClickableViewModel
import shouldBeFalse
import shouldBeTrue
import shouldEqual
import kotlin.test.Test


class TestClickableViewModel {

    @Test
    fun click() {
        var clicked = false
        val viewModel = ClickableViewModel<Unit>()
        viewModel.onClicked += { clicked = true}
        clicked.shouldBeFalse()
        viewModel.click(Unit)
        clicked.shouldBeTrue()
    }

    @Test
    fun clickedStuff() {
        var clicked = 0
        val viewModel = ClickableViewModel<Int>()
        viewModel.onClicked += { clicked += it }
        clicked.shouldEqual(0)
        viewModel.click(1)
        viewModel.click(2)
        clicked.shouldEqual(3)
    }
}