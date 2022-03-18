package com.algolia.instantsearch.android.swiperefreshlayout

import android.os.Build
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.algolia.instantsearch.core.loading.LoadingViewModel
import com.algolia.instantsearch.core.loading.connectView
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@SmallTest
@Config(sdk = [Build.VERSION_CODES.P])
@RunWith(AndroidJUnit4::class)
internal class TestLoadingViewSwipeRefreshLayout {

    @Test
    fun connectShouldUpdateItem() {
        val view = LoadingViewSwipeRefreshLayout(SwipeRefreshLayout(ApplicationProvider.getApplicationContext()))
        val viewModel = LoadingViewModel(true)
        val connection = viewModel.connectView(view)

        assertFalse(view.swipeRefreshLayout.isRefreshing)
        connection.connect()
        assertTrue(view.swipeRefreshLayout.isRefreshing)
    }
}
