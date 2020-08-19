package loading

import android.os.Build
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
@Config(sdk = [Build.VERSION_CODES.P])
@RunWith(AndroidJUnit4::class)
class TestLoadingViewSwipeRefreshLayout {

    private fun view() = LoadingViewSwipeRefreshLayout(SwipeRefreshLayout(applicationContext))

    @Test
    fun connectShouldUpdateItem() {
        val view = view()
        val viewModel = LoadingViewModel(true)
        val connection = viewModel.connectView(view)

        view.swipeRefreshLayout.isRefreshing.shouldBeFalse()
        connection.connect()
        view.swipeRefreshLayout.isRefreshing.shouldBeTrue()
    }
}