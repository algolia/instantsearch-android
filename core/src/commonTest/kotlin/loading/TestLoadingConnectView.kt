package loading

import com.algolia.instantsearch.core.Callback
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

        override var onReload: Callback<Unit>? = null

        override fun setIsLoading(isLoading: Boolean) {
            boolean = isLoading
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
        viewModel.isLoading.value = expected
        view.boolean shouldEqual expected
    }

    @Test
    fun onClickShouldCallEventSubscription() {
        val viewModel = LoadingViewModel()
        val view = MockLoadingView()
        var expected = false

        viewModel.eventReload.subscribe { expected = true }
        view.onReload.shouldBeNull()
        viewModel.connectView(view)
        view.onReload.shouldNotBeNull()
        view.onReload!!(Unit)
        expected.shouldBeTrue()

    }
}