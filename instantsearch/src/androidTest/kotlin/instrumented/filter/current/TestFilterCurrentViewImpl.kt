package instrumented.filter.current

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.algolia.instantsearch.android.filter.current.DefaultFilterCurrentView
import com.algolia.instantsearch.filter.current.FilterAndID
import com.algolia.instantsearch.filter.state.FilterGroupID
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.google.android.material.chip.ChipGroup
import instrumented.applicationContext
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import shouldEqual

@SmallTest
@Config(sdk = [Build.VERSION_CODES.P])
@RunWith(AndroidJUnit4::class)
class TestFilterCurrentViewImpl {

    private val color = Attribute("color")
    private val groupID = FilterGroupID(color)
    private val filterRed = Filter.Facet(color, "red")
    private val filterGreen = Filter.Facet(color, "green")
    private val filters = listOf(
        (groupID to filterRed) to "red",
        (groupID to filterGreen) to "green"
    )

    private fun view() =
        DefaultFilterCurrentView(ChipGroup(applicationContext))

    @Test
    fun onViewClickCallsClearFilters() {
        val view = view()
        var selected: FilterAndID? = null

        view.onFilterSelected = { selected = it }
        view.setFilters(filters)
        view.view.getChildAt(0).callOnClick()
        selected shouldEqual filters.first().first
    }
}
