package clickable

import com.algolia.instantsearch.core.clickable.ClickableViewModel
import shouldBeFalse
import shouldBeTrue
import shouldEqual
import kotlin.test.Test


class TestClickableViewModel {

    @Test
    fun click() {
        val viewModel = ClickableViewModel<Unit>()
        var clicked = false

        viewModel.onClicked += { clicked = true}
        clicked.shouldBeFalse()
        viewModel.click(Unit)
        clicked.shouldBeTrue()
    }

    @Test
    fun clickedStuff() {
        val viewModel = ClickableViewModel<Int>()
        var clicked = 0

        viewModel.onClicked += { clicked += it }
        clicked.shouldEqual(0)
        viewModel.click(1)
        viewModel.click(2)
        clicked.shouldEqual(3)
    }
}