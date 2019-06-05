package loading

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import applicationContext
import com.algolia.instantsearch.core.loading.LoadingViewModel
import com.algolia.instantsearch.core.loading.connectView
import com.algolia.instantsearch.helper.android.loading.LoadingViewSwipeRefreshLayout
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import shouldBeFalse
import shouldBeTrue


@SmallTest
@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class TestLoadingViewSwipeRefreshLayout {

    private fun view() = LoadingViewSwipeRefreshLayout(SwipeRefreshLayout(applicationContext))

    @Test
    fun connectShouldUpdateItem() {
        val view = view()
        val viewModel = LoadingViewModel(true)

        view.swipeRefreshLayout.isRefreshing.shouldBeFalse()
        viewModel.connectView(view)
        view.swipeRefreshLayout.isRefreshing.shouldBeTrue()
    }
}