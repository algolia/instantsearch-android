package instrumented.filter.clear

import android.os.Build
import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.algolia.instantsearch.helper.android.filter.clear.FilterClearViewImpl
import instrumented.applicationContext
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import shouldBeTrue

@SmallTest
@Config(sdk = [Build.VERSION_CODES.P])
@RunWith(AndroidJUnit4::class)
class TestFilterClearViewImpl {

    private fun view() =
        FilterClearViewImpl(View(applicationContext))

    @Test
    fun onViewClickCallsClearFilters() {
        val view = view()
        var isClicked = false

        view.onClear = { isClicked = true }
        view.view.callOnClick()
        isClicked.shouldBeTrue()
    }
}
