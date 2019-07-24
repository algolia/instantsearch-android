package filter

import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import applicationContext
import com.algolia.instantsearch.helper.android.filter.FilterClearViewImpl
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import shouldBeTrue


@SmallTest
@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class TestFilterClearViewImpl {

    private fun view() = FilterClearViewImpl(View(applicationContext))

    @Test
    fun onViewClickCallsClearFilters() {
        val view = view()
        var isClicked = false

        view.onClear = { isClicked = true }
        view.view.callOnClick()
        isClicked.shouldBeTrue()
    }
}