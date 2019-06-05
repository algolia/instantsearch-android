package loading

import com.algolia.instantsearch.core.loading.LoadingView
import com.algolia.instantsearch.core.loading.LoadingViewModel
import com.algolia.instantsearch.core.loading.connectView
import shouldBeNull
import shouldBeTrue
import shouldEqual
import shouldNotBeNull
import kotlin.test.Test


class TestLoadingConnectView {

    private class MockLoadingView : LoadingView {

        var boolean: Boolean? = null

        override var onClick: ((Unit) -> Unit)? = null

        override fun setItem(item: Boolean) {
            boolean = item
        }
    }

    @Test
    fun connectShouldCallSetItem() {
        val expected = true
        val viewModel = LoadingViewModel(expected)
        val view = MockLoadingView()

        view.boolean.shouldBeNull()
        viewModel.connectView(view)
        view.boolean shouldEqual expected
    }

    @Test
    fun onItemChangedShouldCallSetItem() {
        val viewModel = LoadingViewModel()
        val view = MockLoadingView()
        val expected = true

        viewModel.connectView(view)
        view.boolean shouldEqual false
        viewModel.item = expected
        view.boolean shouldEqual expected
    }

    @Test
    fun onClickShouldCallOnTriggerChanged() {
        val viewModel = LoadingViewModel()
        val view = MockLoadingView()
        var expected = false

        viewModel.onTriggered += { expected = true }
        view.onClick.shouldBeNull()
        viewModel.connectView(view)
        view.onClick.shouldNotBeNull()
        view.onClick!!(Unit)
        expected.shouldBeTrue()

    }
}